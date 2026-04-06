package com.thepigcat.spoiler.content.recipe;

import com.thepigcat.spoiler.FSTags;
import com.thepigcat.spoiler.registries.FSRecipes;
import com.thepigcat.spoiler.utils.SpoilingUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SaltPreservationRecipe extends CustomRecipe {
    public SaltPreservationRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
        super(pId, pCategory);
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        ItemStack foodItem = ItemStack.EMPTY;
        boolean hasSalt = false;
        int nonEmpty = 0;

        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack item = pContainer.getItem(i);
            if (item.isEmpty()) continue;
            nonEmpty++;

            if (item.is(FSTags.SALTS)) {
                if (hasSalt) return false;
                hasSalt = true;
            } else if (item.isEdible() && !item.is(FSTags.UNSPOILABLE_FOODS) && item.is(FSTags.SALTABLE_FOODS)) {
                if (!foodItem.isEmpty()) return false;
                foodItem = item;
            } else {
                return false;
            }
        }

        return nonEmpty == 2 && hasSalt && !foodItem.isEmpty() && !SpoilingUtils.isSalted(foodItem);
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
        ItemStack foodItem = ItemStack.EMPTY;

        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack item = pContainer.getItem(i);
            if (item.isEmpty() || item.is(FSTags.SALTS)) continue;
            if (item.isEdible()) {
                foodItem = item;
                break;
            }
        }

        if (foodItem.isEmpty()) return ItemStack.EMPTY;
        ItemStack result = foodItem.copy();
        SpoilingUtils.setSalted(result, true);
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return pWidth * pHeight >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return FSRecipes.SALT_PRESERVATION.get();
    }
}
