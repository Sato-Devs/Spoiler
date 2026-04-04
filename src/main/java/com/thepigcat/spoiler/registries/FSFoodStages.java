package com.thepigcat.spoiler.registries;

import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.FSTags;
import com.thepigcat.spoiler.FoodSpoiling;
import com.thepigcat.spoiler.api.FoodStage;
import com.thepigcat.spoiler.api.FoodStages;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class FSFoodStages {
    private static final Map<ResourceKey<FoodStages>, FoodStages> ENTRIES = new HashMap<>();

    public static final ResourceKey<FoodStages> SEEDS = register("seeds", new FoodStages(Optional.of(FSTags.SEEDS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 30),
            new FoodStage(FSFoodQualities.STALE, 20),
            new FoodStage(FSFoodQualities.WEAK, 15),
            new FoodStage(FSFoodQualities.DEAD, 10),
            new FoodStage(FSFoodQualities.ROTTEN, 5)
    )));

    public static final ResourceKey<FoodStages> RAW_MEATS = register("raw_meats", new FoodStages(Optional.of(FSTags.RAW_MEATS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 4),
            new FoodStage(FSFoodQualities.SPOILED, 3),
            new FoodStage(FSFoodQualities.RANCID, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> COOKED_MEATS = register("cooked_meats", new FoodStages(Optional.of(FSTags.COOKED_MEATS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 8),
            new FoodStage(FSFoodQualities.STALE, 4),
            new FoodStage(FSFoodQualities.SPOILED, 4),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> COOKED_MEALS = register("cooked_meals", new FoodStages(Optional.of(FSTags.COOKED_MEALS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 5),
            new FoodStage(FSFoodQualities.STALE, 4),
            new FoodStage(FSFoodQualities.SPOILED, 4),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> BAKED_GOODS = register("baked_goods", new FoodStages(Optional.of(FSTags.BAKED_GOODS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 6),
            new FoodStage(FSFoodQualities.STALE, 5),
            new FoodStage(FSFoodQualities.MOLDY, 5),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> DESSERTS = register("desserts", new FoodStages(Optional.of(FSTags.DESSERTS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 7),
            new FoodStage(FSFoodQualities.STALE, 6),
            new FoodStage(FSFoodQualities.MOLDY, 5),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> SOUP_STEW = register("soup_stew", new FoodStages(Optional.of(FSTags.SOUP_STEW), List.of(
            new FoodStage(FSFoodQualities.FRESH, 6),
            new FoodStage(FSFoodQualities.STALE, 5),
            new FoodStage(FSFoodQualities.SOUR, 4),
            new FoodStage(FSFoodQualities.RANCID, 3)
    )));

    public static final ResourceKey<FoodStages> ALCOHOLIC_DRINKS = register("alcoholic_drinks", new FoodStages(Optional.of(FSTags.ALCOHOLIC_DRINKS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 50),
            new FoodStage(FSFoodQualities.AGED, 50),
            new FoodStage(FSFoodQualities.FLAT, 50),
            new FoodStage(FSFoodQualities.RANCID, 20)
    )));

    public static final ResourceKey<FoodStages> DRINKS = register("drinks", new FoodStages(Optional.of(FSTags.DRINKS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 4),
            new FoodStage(FSFoodQualities.STALE, 5),
            new FoodStage(FSFoodQualities.SOUR, 4),
            new FoodStage(FSFoodQualities.RANCID, 3)
    )));

    public static final ResourceKey<FoodStages> RAW_EGGS = register("raw_eggs", new FoodStages(Optional.of(FSTags.RAW_EGGS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 8),
            new FoodStage(FSFoodQualities.SPOILED, 4),
            new FoodStage(FSFoodQualities.RANCID, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 2)
    )));

    public static final ResourceKey<FoodStages> COOKED_EGGS = register("cooked_eggs", new FoodStages(Optional.of(FSTags.COOKED_EGGS), List.of(
            new FoodStage(FSFoodQualities.FRESH, 8),
            new FoodStage(FSFoodQualities.STALE, 6),
            new FoodStage(FSFoodQualities.SPOILED, 5),
            new FoodStage(FSFoodQualities.ROTTEN, 4)
    )));

    public static final ResourceKey<FoodStages> LIQUID_DAIRY = register("liquid_dairy", new FoodStages(Optional.of(FSTags.LIQUID_DAIRY), List.of(
            new FoodStage(FSFoodQualities.FRESH, 6),
            new FoodStage(FSFoodQualities.SOUR, 4),
            new FoodStage(FSFoodQualities.RANCID, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 2)
    )));

    public static final ResourceKey<FoodStages> SOFT_DAIRY = register("soft_dairy", new FoodStages(Optional.of(FSTags.SOFT_DAIRY), List.of(
            new FoodStage(FSFoodQualities.FRESH, 8),
            new FoodStage(FSFoodQualities.STALE, 6),
            new FoodStage(FSFoodQualities.MOLDY, 5),
            new FoodStage(FSFoodQualities.RANCID, 4),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> HARD_DAIRY = register("hard_dairy", new FoodStages(Optional.of(FSTags.HARD_DAIRY), List.of(
            new FoodStage(FSFoodQualities.FRESH, 20),
            new FoodStage(FSFoodQualities.STALE, 15),
            new FoodStage(FSFoodQualities.MOLDY, 10),
            new FoodStage(FSFoodQualities.RANCID, 5),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> FAT = register("fat", new FoodStages(Optional.of(FSTags.FAT), List.of(
            new FoodStage(FSFoodQualities.FRESH, 30),
            new FoodStage(FSFoodQualities.STALE, 20),
            new FoodStage(FSFoodQualities.MOLDY, 7),
            new FoodStage(FSFoodQualities.RANCID, 7),
            new FoodStage(FSFoodQualities.ROTTEN, 4)
    )));

    public static final ResourceKey<FoodStages> SOFT_PRODUCE = register("soft_produce", new FoodStages(Optional.of(FSTags.SOFT_PRODUCE), List.of(
            new FoodStage(FSFoodQualities.FRESH, 5),
            new FoodStage(FSFoodQualities.BRUISED, 3),
            new FoodStage(FSFoodQualities.MUSHY, 3),
            new FoodStage(FSFoodQualities.MOLDY, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> HARD_PRODUCE = register("hard_produce", new FoodStages(Optional.of(FSTags.HARD_PRODUCE), List.of(
            new FoodStage(FSFoodQualities.FRESH, 10),
            new FoodStage(FSFoodQualities.BRUISED, 6),
            new FoodStage(FSFoodQualities.MUSHY, 5),
            new FoodStage(FSFoodQualities.MOLDY, 4),
            new FoodStage(FSFoodQualities.ROTTEN, 4)
    )));

    public static final ResourceKey<FoodStages> PICKLED_PRODUCE = register("pickled_produce", new FoodStages(Optional.of(FSTags.PICKLED_PRODUCE), List.of(
            new FoodStage(FSFoodQualities.FRESH, 30),
            new FoodStage(FSFoodQualities.AGED, 20),
            new FoodStage(FSFoodQualities.SOUR, 10),
            new FoodStage(FSFoodQualities.RANCID, 5)
    ), false));

    public static final ResourceKey<FoodStages> DRY_PRODUCE = register("dry_produce", new FoodStages(Optional.of(FSTags.DRY_PRODUCE), List.of(
            new FoodStage(FSFoodQualities.FRESH, 20),
            new FoodStage(FSFoodQualities.STALE, 15),
            new FoodStage(FSFoodQualities.MOLDY, 10),
            new FoodStage(FSFoodQualities.ROTTEN, 5)
    )));

    public static final ResourceKey<FoodStages> PRODUCE = register("produce", new FoodStages(Optional.of(FSTags.PRODUCE), List.of(
            new FoodStage(FSFoodQualities.FRESH, 6),
            new FoodStage(FSFoodQualities.BRUISED, 5),
            new FoodStage(FSFoodQualities.MUSHY, 4),
            new FoodStage(FSFoodQualities.MOLDY, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    public static final ResourceKey<FoodStages> DEFAULT = register("default", new FoodStages(Optional.empty(), List.of(
            new FoodStage(FSFoodQualities.FRESH, 8),
            new FoodStage(FSFoodQualities.STALE, 5),
            new FoodStage(FSFoodQualities.SPOILED, 4),
            new FoodStage(FSFoodQualities.MOLDY, 3),
            new FoodStage(FSFoodQualities.ROTTEN, 3)
    )));

    private static void register(BootstapContext<FoodStages> context, ResourceKey<FoodStages> key, FoodStages builder) {
        context.register(key, builder);
    }

    private static ResourceKey<FoodStages> key(String name) {
        return ResourceKey.create(FSRegistries.FOOD_STAGES_KEY, FoodSpoiling.rl(name));
    }

    private static ResourceKey<FoodStages> register(String name, FoodStages foodStages) {
        ResourceKey<FoodStages> key = key(name);
        ENTRIES.put(key, foodStages);
        return key;
    }

    public static void bootstrap(BootstapContext<FoodStages> context) {
        for (Map.Entry<ResourceKey<FoodStages>, FoodStages> entries : ENTRIES.entrySet()) {
            register(context, entries.getKey(), entries.getValue());
        }
    }
}
