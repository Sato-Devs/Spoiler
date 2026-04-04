package com.thepigcat.spoiler.registries;

import com.thepigcat.spoiler.FoodSpoiling;
import com.thepigcat.spoiler.content.recipe.FoodCombinationCrafting;
import com.thepigcat.spoiler.content.recipe.SaltPreservationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class FSRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FoodSpoiling.MODID);

    public static final RegistryObject<SimpleCraftingRecipeSerializer<FoodCombinationCrafting>> FOOD_COMBINATION = RECIPES.register("food_combination", () -> new SimpleCraftingRecipeSerializer<>(FoodCombinationCrafting::new));
    public static final RegistryObject<SimpleCraftingRecipeSerializer<SaltPreservationRecipe>> SALT_PRESERVATION = RECIPES.register("salt_preservation", () -> new SimpleCraftingRecipeSerializer<>(SaltPreservationRecipe::new));
}
