package com.thepigcat.spoiler;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public final class FSTags {
    public static final TagKey<Item> UNSPOILABLE_FOODS = bindItemTag("unspoilable_foods");
    public static final TagKey<Item> RAW_MEATS = bindItemTag("raw_meats");
    public static final TagKey<Item> COOKED_MEATS = bindItemTag("cooked_meats");
    public static final TagKey<Item> COOKED_MEALS = bindItemTag("cooked_meals");
    public static final TagKey<Item> BAKED_GOODS = bindItemTag("baked_goods");
    public static final TagKey<Item> DESSERTS = bindItemTag("desserts");
    public static final TagKey<Item> SOUP_STEW = bindItemTag("soup_stew");
    public static final TagKey<Item> ALCOHOLIC_DRINKS = bindItemTag("alcoholic_drinks");
    public static final TagKey<Item> DRINKS = bindItemTag("drinks");
    public static final TagKey<Item> RAW_EGGS = bindItemTag("raw_eggs");
    public static final TagKey<Item> COOKED_EGGS = bindItemTag("cooked_eggs");
    public static final TagKey<Item> LIQUID_DAIRY = bindItemTag("liquid_dairy");
    public static final TagKey<Item> SOFT_DAIRY = bindItemTag("soft_dairy");
    public static final TagKey<Item> HARD_DAIRY = bindItemTag("hard_dairy");
    public static final TagKey<Item> FAT = bindItemTag("fat");
    public static final TagKey<Item> SEEDS = bindItemTag("seeds");
    public static final TagKey<Item> PRODUCE = bindItemTag("produce");
    public static final TagKey<Item> SOFT_PRODUCE = bindItemTag("soft_produce");
    public static final TagKey<Item> HARD_PRODUCE = bindItemTag("hard_produce");
    public static final TagKey<Item> DRY_PRODUCE = bindItemTag("dry_produce");
    public static final TagKey<Item> PICKLED_PRODUCE = bindItemTag("pickled_produce");
    public static final TagKey<Item> SALTS = bindItemTag("salts");
    public static final TagKey<EntityType<?>> ENTITIES_WITH_INVENTORY = bindEntityTypeTag("entities_with_inventory");

    private static @NotNull TagKey<Item> bindItemTag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(FoodSpoiling.MODID, path));
    }

    private static @NotNull TagKey<EntityType<?>> bindEntityTypeTag(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(FoodSpoiling.MODID, path));
    }
}
