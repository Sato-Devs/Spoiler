package com.thepigcat.spoiler.mixins;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.FSTags;
import com.thepigcat.spoiler.api.FoodQuality;
import com.thepigcat.spoiler.api.FoodStage;
import com.thepigcat.spoiler.utils.NBTSpoilingUtils;
import com.thepigcat.spoiler.utils.SpoilingUtils;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodData.class)
public class FoodDataMixin {
    @Shadow private int foodLevel;
    @Shadow private float saturationLevel;

    @Inject(method = "Lnet/minecraft/world/food/FoodData;eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void spoiling$eat(Item foodItem, ItemStack foodStack, LivingEntity entity, CallbackInfo ci) {
        if (NBTSpoilingUtils.hasFoodState(foodStack) && !foodStack.is(FSTags.UNSPOILABLE_FOODS) && foodStack.isEdible()) {
            Level level = entity.level();
            FoodStage curStage = SpoilingUtils.getCurStage(foodStack, level.registryAccess());
            if (curStage == null) {
                curStage = SpoilingUtils.getLastStage(foodStack, level.registryAccess());
            }
            if (curStage == null) return;
            Holder.Reference<FoodQuality> quality = level.registryAccess().lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).getOrThrow(curStage.quality());

            for (Pair<Either<MobEffectInstance, Potion>, Float> pair : quality.value().effects()) {
                Either<MobEffectInstance, Potion> effect = pair.getFirst();
                if (!level.isClientSide && effect != null && level.random.nextFloat() < pair.getSecond()) {
                    if (effect.left().isPresent()) {
                        MobEffectInstance original = effect.left().get();
                        entity.addEffect(new MobEffectInstance(original.getEffect(), original.getDuration(), original.getAmplifier(), original.isAmbient(), original.isVisible(), original.showIcon()));
                    } else if (effect.right().isPresent()) {
                        for (MobEffectInstance mobEffectInstance : effect.right().get().getEffects()) {
                            entity.addEffect(new MobEffectInstance(mobEffectInstance.getEffect(), mobEffectInstance.getDuration(), mobEffectInstance.getAmplifier(), mobEffectInstance.isAmbient(), mobEffectInstance.isVisible(), mobEffectInstance.showIcon()));
                        }
                    }
                }
            }

            FoodProperties foodproperties = foodStack.getFoodProperties(entity);
            int nutrition = Math.max(1, (int) (foodproperties.getNutrition() * quality.value().nutritionMod()));
            float saturationModifier = foodproperties.getSaturationModifier() * quality.value().saturationMod();
            this.foodLevel = Math.min(nutrition + this.foodLevel, 20);
            this.saturationLevel = Math.min(this.saturationLevel + (float) nutrition * saturationModifier * 2.0F, (float) this.foodLevel);

            ci.cancel();
        }
    }
}
