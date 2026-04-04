package com.thepigcat.spoiler.registries;

import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.FoodSpoiling;
import com.thepigcat.spoiler.api.FoodQuality;
import com.thepigcat.spoiler.utils.RGBAColor;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public final class FSFoodQualities {
    private static final Map<ResourceKey<FoodQuality>, FoodQuality.Builder> ENTRIES = new HashMap<>();

    public static final ResourceKey<FoodQuality> FRESH = register("fresh", builder -> builder
            .textColor(new RGBAColor(87, 209, 83))
            .tintColor(new RGBAColor(255, 255, 255))
            .saturation(1.0f)
            .nutrition(1.0f)
            .usableInRecipes(FoodQuality.RecipeType.ALL)
    );

    public static final ResourceKey<FoodQuality> STALE = register("stale", builder -> builder
            .textColor(new RGBAColor(200, 190, 120))
            .tintColor(new RGBAColor(220, 210, 150, 80))
            .saturation(0.9f)
            .nutrition(0.9f)
    );

    public static final ResourceKey<FoodQuality> BRUISED = register("bruised", builder -> builder
            .textColor(new RGBAColor(180, 160, 90))
            .tintColor(new RGBAColor(190, 170, 100, 70))
            .saturation(0.9f)
            .nutrition(0.9f)
    );

    public static final ResourceKey<FoodQuality> AGED = register("aged", builder -> builder
            .textColor(new RGBAColor(190, 150, 70))
            .tintColor(new RGBAColor(200, 160, 80, 60))
            .saturation(0.9f)
            .nutrition(0.9f)
            .effects(new MobEffectInstance(MobEffects.ABSORPTION, 600, 0), 0.75f)
    );

    public static final ResourceKey<FoodQuality> FLAT = register("flat", builder -> builder
            .textColor(new RGBAColor(170, 160, 140))
            .tintColor(new RGBAColor(180, 170, 150, 70))
            .saturation(0.8f)
            .nutrition(0.8f)
    );

    public static final ResourceKey<FoodQuality> MUSHY = register("mushy", builder -> builder
            .textColor(new RGBAColor(160, 145, 80))
            .tintColor(new RGBAColor(170, 155, 90, 80))
            .saturation(0.5f)
            .nutrition(0.5f)
    );

    public static final ResourceKey<FoodQuality> SPOILED = register("spoiled", builder -> builder
            .textColor(new RGBAColor(180, 170, 60))
            .tintColor(new RGBAColor(160, 150, 50, 90))
            .saturation(0.5f)
            .nutrition(0.5f)
            .effects(new MobEffectInstance(MobEffects.HUNGER, 400, 0), 0.75f)
            .effects(new MobEffectInstance(MobEffects.POISON, 200, 0), 0.3f)
    );

    public static final ResourceKey<FoodQuality> SOUR = register("sour", builder -> builder
            .textColor(new RGBAColor(190, 200, 50))
            .tintColor(new RGBAColor(180, 190, 60, 80))
            .saturation(0.5f)
            .nutrition(0.5f)
            .effects(new MobEffectInstance(MobEffects.HUNGER, 300, 0), 0.5f)
    );

    public static final ResourceKey<FoodQuality> WEAK = register("weak", builder -> builder
            .textColor(new RGBAColor(150, 140, 100))
            .tintColor(new RGBAColor(160, 150, 110, 80))
            .saturation(0.6f)
            .nutrition(0.6f)
    );

    public static final ResourceKey<FoodQuality> MOLDY = register("moldy", builder -> builder
            .textColor(new RGBAColor(100, 140, 70))
            .tintColor(new RGBAColor(80, 130, 60, 100))
            .saturation(0.7f)
            .nutrition(0.7f)
            .usableInRecipes(FoodQuality.RecipeType.NONE)
            .effects(new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.75f)
            .effects(new MobEffectInstance(MobEffects.POISON, 300, 0), 0.5f)
    );

    public static final ResourceKey<FoodQuality> RANCID = register("rancid", builder -> builder
            .textColor(new RGBAColor(130, 110, 50))
            .tintColor(new RGBAColor(120, 100, 40, 110))
            .saturation(0.3f)
            .nutrition(0.3f)
            .usableInRecipes(FoodQuality.RecipeType.NONE)
            .effects(new MobEffectInstance(MobEffects.HUNGER, 800, 1), 0.75f)
            .effects(new MobEffectInstance(MobEffects.POISON, 400, 0), 0.6f)
    );

    public static final ResourceKey<FoodQuality> DEAD = register("dead", builder -> builder
            .textColor(new RGBAColor(100, 90, 70))
            .tintColor(new RGBAColor(90, 80, 60, 120))
            .saturation(0.2f)
            .nutrition(0.2f)
            .usableInRecipes(FoodQuality.RecipeType.NONE)
    );

    public static final ResourceKey<FoodQuality> ROTTEN = register("rotten", builder -> builder
            .textColor(new RGBAColor(80, 100, 40))
            .tintColor(new RGBAColor(60, 90, 30, 130))
            .saturation(0.1f)
            .nutrition(0.1f)
            .usableInRecipes(FoodQuality.RecipeType.NONE)
            .effects(new MobEffectInstance(MobEffects.HUNGER, 1200, 2), 0.9f)
            .effects(new MobEffectInstance(MobEffects.POISON, 600, 1), 0.75f)
    );

    private static void register(BootstapContext<FoodQuality> context, ResourceKey<FoodQuality> key, FoodQuality.Builder builder) {
        context.register(key, builder.build());
    }

    private static ResourceKey<FoodQuality> key(String name) {
        return ResourceKey.create(FSRegistries.FOOD_QUALITY_KEY, FoodSpoiling.rl(name));
    }

    private static ResourceKey<FoodQuality> register(String name, UnaryOperator<FoodQuality.Builder> builder) {
        ResourceKey<FoodQuality> key = key(name);
        ENTRIES.put(key, builder.apply(FoodQuality.builder()));
        return key;
    }

    public static void bootstrap(BootstapContext<FoodQuality> context) {
        for (Map.Entry<ResourceKey<FoodQuality>, FoodQuality.Builder> entries : ENTRIES.entrySet()) {
            register(context, entries.getKey(), entries.getValue());
        }
    }
}
