package com.thepigcat.spoiler.datagen;

import com.thepigcat.spoiler.FSTags;
import com.thepigcat.spoiler.FoodSpoiling;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FSTagsProvider {
    public static class EntityTypeProvider extends TagsProvider<EntityType<?>> {
        protected EntityTypeProvider(PackOutput p_256596_, CompletableFuture<HolderLookup.Provider> p_256513_, @Nullable ExistingFileHelper existingFileHelper) {
            super(p_256596_, Registries.ENTITY_TYPE, p_256513_, FoodSpoiling.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(FSTags.ENTITIES_WITH_INVENTORY, EntityType.PLAYER, EntityType.DONKEY, EntityType.MULE, EntityType.LLAMA, EntityType.CHEST_MINECART, EntityType.HOPPER_MINECART, EntityType.ITEM, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME);
        }

        public void tag(TagKey<EntityType<?>> entityTypeTagKey, EntityType<?>... entityTypes) {
            TagAppender<EntityType<?>> tag = tag(entityTypeTagKey);
            for (EntityType<?> e : entityTypes) {
                tag.add(BuiltInRegistries.ENTITY_TYPE.getResourceKey(e).get());
            }
        }
    }

    public static class FSBlockTagsProvider extends BlockTagsProvider {
        public FSBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, FoodSpoiling.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }
    }

    public static class FSItemTagsProvider extends ItemTagsProvider {
        public FSItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags, FoodSpoiling.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(FSTags.UNSPOILABLE_FOODS)
                    .add(Items.GOLDEN_APPLE)
                    .add(Items.ENCHANTED_GOLDEN_APPLE)
                    .add(Items.GOLDEN_CARROT);

            tag(FSTags.RAW_MEATS)
                    .add(Items.PORKCHOP)
                    .add(Items.BEEF)
                    .add(Items.CHICKEN)
                    .add(Items.RABBIT)
                    .add(Items.MUTTON);

            tag(FSTags.COOKED_MEATS)
                    .add(Items.COOKED_BEEF)
                    .add(Items.COOKED_PORKCHOP)
                    .add(Items.COOKED_CHICKEN)
                    .add(Items.COOKED_RABBIT)
                    .add(Items.COOKED_MUTTON)
                    .add(Items.COOKED_COD)
                    .add(Items.COOKED_SALMON);

            tag(FSTags.COOKED_MEALS)
                    .add(Items.RABBIT_STEW);

            tag(FSTags.BAKED_GOODS)
                    .add(Items.BREAD)
                    .add(Items.PUMPKIN_PIE);

            tag(FSTags.DESSERTS)
                    .add(Items.COOKIE);

            tag(FSTags.SOUP_STEW)
                    .add(Items.MUSHROOM_STEW)
                    .add(Items.BEETROOT_SOUP)
                    .add(Items.SUSPICIOUS_STEW);

            tag(FSTags.DRINKS)
                    .add(Items.HONEY_BOTTLE);

            tag(FSTags.RAW_EGGS)
                    .add(Items.EGG);

            tag(FSTags.HARD_PRODUCE)
                    .add(Items.APPLE)
                    .add(Items.CARROT)
                    .add(Items.POTATO)
                    .add(Items.BEETROOT);

            tag(FSTags.SOFT_PRODUCE)
                    .add(Items.SWEET_BERRIES)
                    .add(Items.GLOW_BERRIES)
                    .add(Items.MELON_SLICE)
                    .add(Items.CHORUS_FRUIT);

            tag(FSTags.DRY_PRODUCE)
                    .add(Items.DRIED_KELP);

            tag(FSTags.PRODUCE)
                    .add(Items.APPLE)
                    .add(Items.SWEET_BERRIES)
                    .add(Items.GLOW_BERRIES)
                    .add(Items.CHORUS_FRUIT)
                    .add(Items.MELON_SLICE);

            tag(FSTags.SEEDS)
                    .add(Items.WHEAT_SEEDS)
                    .add(Items.BEETROOT_SEEDS)
                    .add(Items.MELON_SEEDS)
                    .add(Items.PUMPKIN_SEEDS);

            tag(FSTags.LIQUID_DAIRY)
                    .add(Items.MILK_BUCKET);

            tag(FSTags.ALCOHOLIC_DRINKS);
            tag(FSTags.COOKED_EGGS);
            tag(FSTags.SOFT_DAIRY);
            tag(FSTags.HARD_DAIRY);
            tag(FSTags.FAT);
            tag(FSTags.PICKLED_PRODUCE);
            tag(FSTags.SALTS);

            tag(FSTags.SALTABLE_FOODS)
                    .addTag(FSTags.RAW_MEATS)
                    .addTag(FSTags.COOKED_MEATS)
                    .addTag(FSTags.COOKED_MEALS)
                    .addTag(FSTags.BAKED_GOODS)
                    .addTag(FSTags.COOKED_EGGS)
                    .addTag(FSTags.HARD_PRODUCE)
                    .addTag(FSTags.SOFT_PRODUCE)
                    .addTag(FSTags.DRY_PRODUCE)
                    .addTag(FSTags.PRODUCE);
        }
    }
}
