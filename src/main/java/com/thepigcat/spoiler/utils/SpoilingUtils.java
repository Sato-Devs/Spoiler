package com.thepigcat.spoiler.utils;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.FoodSpoilingConfig;
import com.thepigcat.spoiler.api.FoodQuality;
import com.thepigcat.spoiler.api.FoodStage;
import com.thepigcat.spoiler.api.FoodStages;
import com.thepigcat.spoiler.compat.ColdSweatCompat;
import com.thepigcat.spoiler.compat.LSOCompat;
import com.thepigcat.spoiler.registries.FSFoodStages;
import com.thepigcat.spoiler.registries.FSItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class SpoilingUtils {
    public static final String SALTED_KEY = "salted";
    public static final float SALT_SPOILAGE_MULTIPLIER = 0.5f;

    public static void initialize(ItemStack stack, long dayTime, float spoilingModifier, HolderLookup.Provider lookup) {
        ResourceKey<FoodStages> stages = getFoodStagesResourceKey(stack, lookup);

        NBTSpoilingUtils.initItem(stack);
        NBTSpoilingUtils.setCreationTime(stack, dayTime);
        NBTSpoilingUtils.setLastDayTime(stack, dayTime);
        NBTSpoilingUtils.setFoodStages(stack, stages);
        NBTSpoilingUtils.setMaxSpoilingProgress(stack, getMaxProgress(stack, lookup));
        NBTSpoilingUtils.setSpoilingModifier(stack, spoilingModifier);
    }

    private static @NotNull ResourceKey<FoodStages> getFoodStagesResourceKey(ItemStack stack, HolderLookup.Provider lookup) {
        List<Holder.Reference<FoodStages>> foodStageElements = lookup.lookupOrThrow(FSRegistries.FOOD_STAGES_KEY).listElements().toList();
        ResourceKey<FoodStages> stages = null;
        for (Holder.Reference<FoodStages> foodStages : foodStageElements) {
            Optional<TagKey<Item>> key = foodStages.value().key();
            if (key.isPresent() && stack.is(key.get())) {
                stages = foodStages.key();
                break;
            }
        }

        if (stages == null) {
            stages = FSFoodStages.DEFAULT;
        }

        return stages;
    }

    public static int getMaxProgress(ItemStack stack, HolderLookup.Provider lookup) {
        FoodStages stages = getStages(stack, lookup);
        if (stages != null) {
            int totalDays = 0;
            for (FoodStage stage : stages.stages()) {
                totalDays += stage.days();
            }
            return totalDays * 24000;
        }
        return -1;
    }

    public static FoodStage getCurStage(ItemStack stack, HolderLookup.Provider lookup) {
        FoodStages stages = getStages(stack, lookup);
        if (stages != null) {
            float spoilingProgress = NBTSpoilingUtils.getSpoilingProgress(stack);
            int progressDays = (int) (spoilingProgress / 24000);
            int cumulativeDays = 0;
            for (FoodStage stage : stages.stages()) {
                cumulativeDays += stage.days();
                if (progressDays < cumulativeDays) {
                    return stage;
                }
            }
            return stages.stages().get(stages.stages().size() - 1);
        }
        return null;
    }

    public static int getCurStageIndex(ItemStack stack, HolderLookup.Provider lookup) {
        FoodStages stages = getStages(stack, lookup);
        if (stages != null) {
            float spoilingProgress = NBTSpoilingUtils.getSpoilingProgress(stack);
            int progressDays = (int) (spoilingProgress / 24000);
            int cumulativeDays = 0;
            for (int i = 0; i < stages.stages().size(); i++) {
                cumulativeDays += stages.stages().get(i).days();
                if (progressDays < cumulativeDays) {
                    return i;
                }
            }
            return stages.stages().size() - 1;
        }
        return -1;
    }

    public static float getTemperature(Level level, BlockPos pos) {
        if (hasColdSweat()) {
            return (float) ColdSweatCompat.getTempFahrenheit(ColdSweatCompat.getTemperature(level, pos));
        }
        if (hasLSO()) {
            return LSOCompat.getTemperature(level, pos);
        }
        return 1f;
    }

    public static boolean hasColdSweat() {
        return ModList.get().isLoaded("cold_sweat");
    }

    public static boolean hasLSO() {
        return ModList.get().isLoaded("legendarysurvivaloverhaul");
    }

    public static FoodStages getStages(ItemStack itemStack, HolderLookup.Provider lookup) {
        if (NBTSpoilingUtils.hasFoodState(itemStack)) {
            ResourceKey<FoodStages> foodStages = NBTSpoilingUtils.getFoodStages(itemStack);
            if (foodStages != null) {
                HolderLookup.RegistryLookup<FoodStages> registryLookup = lookup.lookupOrThrow(FSRegistries.FOOD_STAGES_KEY);
                Holder.Reference<FoodStages> foodStagesReference = registryLookup.get(foodStages).orElse(null);
                if (foodStagesReference == null) {
                    foodStagesReference = registryLookup.getOrThrow(getFoodStagesResourceKey(itemStack, lookup));
                }
                return foodStagesReference.value();
            }
        }
        return null;
    }

    public static boolean shouldBecomeRottenItem(ItemStack stack, HolderLookup.Provider lookup) {
        FoodStages stages = getStages(stack, lookup);
        return stages != null && stages.becomeRottenItem();
    }

    public static ItemStack createRottenMass(ItemStack foodStack) {
        ItemStack stack = new ItemStack(FSItems.ROTTEN_MASS.get(), foodStack.getCount());
        NBTSpoilingUtils.setFoodState(stack, NBTSpoilingUtils.getFoodState(foodStack));
        return stack;
    }

    public static ItemStack createDecomposedGoo(ItemStack rottenMassStack) {
        return new ItemStack(FSItems.DECOMPOSED_GOO.get(), rottenMassStack.getCount());
    }

    public static boolean isSalted(ItemStack stack) {
        return stack.hasTag() && stack.getTag().getBoolean(SALTED_KEY);
    }

    public static void setSalted(ItemStack stack, boolean salted) {
        stack.getOrCreateTag().putBoolean(SALTED_KEY, salted);
    }

    public static List<Component> getSpoilingTooltip(ItemStack stack, Player player, boolean isShiftDown) {
        Level level = player.level();
        RegistryAccess access = level.registryAccess();
        ResourceKey<FoodStages> foodStagesType = NBTSpoilingUtils.getFoodStages(stack);
        if (foodStagesType == null) return Collections.emptyList();

        HolderLookup.RegistryLookup<FoodStages> registryLookup = access.lookupOrThrow(FSRegistries.FOOD_STAGES_KEY);
        Holder.Reference<FoodStages> foodStagesReference = registryLookup.get(foodStagesType).orElse(null);
        if (foodStagesReference == null) {
            foodStagesReference = registryLookup.getOrThrow(getFoodStagesResourceKey(stack, access));
        }
        List<FoodStage> stages = foodStagesReference.value().stages();
        if (stages.isEmpty()) return Collections.emptyList();

        float spoilingProgress = NBTSpoilingUtils.getSpoilingProgress(stack);
        float maxSpoilingProgress = NBTSpoilingUtils.getMaxSpoilingProgress(stack);

        FoodStage curStage = getCurStage(stack, access);
        int curIndex = getCurStageIndex(stack, access);
        FoodStage nextStage = (curIndex >= 0 && curIndex + 1 < stages.size()) ? stages.get(curIndex + 1) : null;

        ResourceKey<FoodQuality> key = curStage != null ? curStage.quality() : stages.get(stages.size() - 1).quality();
        Holder.Reference<FoodQuality> quality = access.lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).getOrThrow(key);
        float freshness = getFreshness(spoilingProgress, maxSpoilingProgress);

        int totalDays = 0;
        for (FoodStage s : stages) totalDays += s.days();
        long creationTime = NBTSpoilingUtils.getCreationTime(stack);
        long expirationDate = creationTime + ((long) totalDays * 24000L);
        int expirationDay = (int) (expirationDate / 24000);
        String expirationDateText = String.format("Day %d - %s", expirationDay, timeToHoursMinutes(level, expirationDate));

        List<Component> tooltip = new ArrayList<>();
        if (NBTSpoilingUtils.getSpoilingModifier(stack) == 0) {
            tooltip.add(Component.literal("Frozen").withStyle(Style.EMPTY.withColor(FoodSpoilingConfig.frozenTintColor)));
        }
        if (isSalted(stack)) {
            tooltip.add(Component.literal("Salted").withStyle(ChatFormatting.GOLD));
        }
        tooltip.addAll(List.of(
                Component.literal("Quality: ").withStyle(ChatFormatting.GRAY).append(registryTranslation(key).copy().withStyle(Style.EMPTY.withColor(quality.value().textColor().toARGB()))),
                Component.literal("Freshness: ").withStyle(ChatFormatting.GRAY).append(Math.round(freshness * 100) + "%"),
                Component.literal("Expires on: ").withStyle(ChatFormatting.GRAY).append(Component.literal(expirationDateText).withStyle(ChatFormatting.YELLOW))
        ));
        if (!isShiftDown) {
            tooltip.add(Component.literal("Hold <Shift> for more information").withStyle(ChatFormatting.GRAY));
        } else if (curStage != null) {
            tooltip.addAll(createAdvancedTooltip(stack, player, curStage, nextStage, curIndex, stages, access));
        }
        return tooltip;
    }

    public static FoodStage getLastStage(ItemStack stack, HolderLookup.Provider lookup) {
        FoodStages stages = getStages(stack, lookup);
        return stages != null ? stages.stages().get(stages.stages().size() - 1) : null;
    }

    public static float getFreshness(float progress, float maxProgress) {
        if (maxProgress <= 0) return 1f;
        return Math.max(0f, 1f - (progress / maxProgress));
    }

    private static List<Component> createAdvancedTooltip(ItemStack stack, Player player, FoodStage curStage, FoodStage nextStage, int curIndex, List<FoodStage> stages, RegistryAccess access) {
        Holder.Reference<FoodQuality> quality = access.lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).getOrThrow(curStage.quality());
        List<Component> advTooltip = new ArrayList<>();

        float spoilingProgress = NBTSpoilingUtils.getSpoilingProgress(stack);
        int cumulativeDaysBefore = 0;
        for (int i = 0; i < curIndex; i++) {
            cumulativeDaysBefore += stages.get(i).days();
        }
        int cumulativeDaysCurrent = cumulativeDaysBefore + curStage.days();
        int progressDays = (int) (spoilingProgress / 24000);
        int daysRemaining = cumulativeDaysCurrent - progressDays;

        MutableComponent nextStageComponent = Component.empty();
        if (nextStage != null) {
            Holder.Reference<FoodQuality> nextQuality = access.lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).getOrThrow(nextStage.quality());
            nextStageComponent.append(Component.literal("Next Stage ")
                    .append(Component.literal(registryTranslation(nextQuality.key()).getString()).withStyle(Style.EMPTY.withColor(nextQuality.value().textColor().toARGB()))));
        } else {
            nextStageComponent.append(Component.literal("Decayed").withStyle(ChatFormatting.DARK_RED));
        }
        nextStageComponent.append(Component.literal(daysRemaining > 0 ? " in " + daysRemaining + " Days" : " in < 1 Day")).withStyle(ChatFormatting.GRAY);
        advTooltip.add(nextStageComponent);

        FoodProperties foodProperties = stack.getFoodProperties(player);
        if (foodProperties != null) {
            advTooltip.add(Component.literal("Saturation: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(String.format("%.0f%%", quality.value().saturationMod() * 100)).withStyle(ChatFormatting.WHITE)));
            advTooltip.add(Component.literal("Nutrition: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(String.format("%.0f%%", quality.value().nutritionMod() * 100)).withStyle(ChatFormatting.WHITE)));
            if (!quality.get().effects().isEmpty()) {
                advTooltip.add(Component.literal("Effects:").withStyle(ChatFormatting.GRAY));
                advTooltip.addAll(formatEffects(quality.get().effects()));
            }
        }
        return advTooltip;
    }

    private static List<Component> formatEffects(List<Pair<Either<MobEffectInstance, Potion>, Float>> effects) {
        List<Component> tooltip = new ArrayList<>();
        for (Pair<Either<MobEffectInstance, Potion>, Float> effect : effects) {
            Either<MobEffectInstance, Potion> effectFirst = effect.getFirst();
            int chance = Math.round(effect.getSecond() * 100);
            if (effectFirst.left().isPresent()) {
                MobEffectInstance inst = effectFirst.left().get();
                String duration = String.format("(%ds)", inst.getDuration() / 20);
                tooltip.add(Component.literal(" ")
                        .append(Component.translatable(inst.getDescriptionId()))
                        .append(Component.literal(" " + duration + " " + chance + "%"))
                        .withStyle(ChatFormatting.DARK_GRAY));
            } else if (effectFirst.right().isPresent()) {
                ResourceLocation potionKey = BuiltInRegistries.POTION.getKey(effectFirst.right().get());
                tooltip.add(Component.literal(" ")
                        .append(Component.translatable("item." + potionKey.getNamespace() + ".potion.effect." + potionKey.getPath()))
                        .append(Component.literal(" " + chance + "%"))
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
        return tooltip;
    }

    private static String timeToHoursMinutes(Level world, long expirationDate) {
        int time = ((int) (expirationDate) % 24000);
        int m = (int) (((time % 1000f) / 1000f) * 60);
        int h = time / 1000;

        String ob = "";
        String br = "";
        if (!world.dimensionType().natural()) {
            ob += ChatFormatting.OBFUSCATED;
            br += ChatFormatting.RESET;
        }
        return ob + h + br + ":" + ob + ((m < 10) ? "0" : "") + m + br;
    }

    public static <T> Component registryTranslation(ResourceKey<T> key) {
        return Component.translatable(key.registry().getPath() + "." + key.location().getNamespace() + "." + key.location().getPath());
    }

    public static <T> Component registryTranslation(Registry<T> registry, T registryObject) {
        ResourceLocation objLoc = registry.getKey(registryObject);
        return Component.translatable(registry.key().location().getPath() + "." + objLoc.getNamespace() + "." + objLoc.getPath());
    }

    public static float getContainerSpoilageModifier(ResourceLocation blockentityId) {
        if (FoodSpoilingConfig.containerModifiers.containsKey(blockentityId)) {
            return FoodSpoilingConfig.containerModifiers.get(blockentityId);
        }

        return 1.0f;
    }
}
