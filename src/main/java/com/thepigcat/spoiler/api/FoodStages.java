package com.thepigcat.spoiler.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Optional;

public record FoodStages(Optional<TagKey<Item>> key, List<FoodStage> stages, boolean becomeRottenItem) {
    public FoodStages(Optional<TagKey<Item>> key, List<FoodStage> stages) {
        this(key, stages, true);
    }

    public static final Codec<FoodStages> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            TagKey.codec(Registries.ITEM).optionalFieldOf("items").forGetter(FoodStages::key),
            FoodStage.CODEC.listOf().fieldOf("stages").forGetter(FoodStages::stages),
            Codec.BOOL.optionalFieldOf("become_rotten_item", true).forGetter(FoodStages::becomeRottenItem)
    ).apply(inst, FoodStages::new));
}
