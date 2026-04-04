package com.thepigcat.spoiler.events;

import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.FoodSpoiling;
import com.thepigcat.spoiler.FoodSpoilingConfig;
import com.thepigcat.spoiler.api.FoodStage;
import com.thepigcat.spoiler.utils.NBTSpoilingUtils;
import com.thepigcat.spoiler.utils.SpoilingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FoodSpoiling.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FSClientModEvents {
    private static final ResourceLocation SALTED_OVERLAY = new ResourceLocation(FoodSpoiling.MODID, "textures/item/salted_overlay.png");

    @SubscribeEvent
    public static void onTintItem(RegisterColorHandlersEvent.Item event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.isEdible()) {
                event.register((stack, layer) -> {
                    if (NBTSpoilingUtils.hasFoodState(stack)) {
                        if (FoodSpoilingConfig.renderFrozenOverlay) {
                            float spoilingModifier = NBTSpoilingUtils.getSpoilingModifier(stack);
                            if (spoilingModifier == 0) {
                                return FoodSpoilingConfig.frozenTintColor;
                            }
                        }

                        if (FoodSpoilingConfig.renderSpoiledOverlay) {
                            Level level = Minecraft.getInstance().level;
                            if (level != null) {
                                RegistryAccess lookup = level.registryAccess();
                                FoodStage curStage = SpoilingUtils.getCurStage(stack, lookup);
                                if (curStage != null) {
                                    return lookup.lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).getOrThrow(curStage.quality()).value().tintColor().toARGB();
                                }
                            }
                        }
                    }
                    return -1;
                }, item);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.isEdible()) {
                event.register(item, (guiGraphics, font, stack, xOffset, yOffset) -> {
                    if (SpoilingUtils.isSalted(stack)) {
                        renderSaltedOverlay(guiGraphics, xOffset, yOffset);
                        return true;
                    }
                    return false;
                });
            }
        }
    }

    private static void renderSaltedOverlay(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(SALTED_OVERLAY, x, y, 0, 0, 16, 16, 16, 16);
    }
}
