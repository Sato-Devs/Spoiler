package com.thepigcat.spoiler.datagen;

import com.thepigcat.spoiler.FoodSpoiling;
import com.thepigcat.spoiler.api.FoodQuality;
import com.thepigcat.spoiler.registries.FSFoodQualities;
import com.thepigcat.spoiler.registries.FSItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.data.LanguageProvider;

public class FSEnUSLangProvider extends LanguageProvider {
    public FSEnUSLangProvider(PackOutput output) {
        super(output, FoodSpoiling.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(FSFoodQualities.FRESH, "Fresh");
        add(FSFoodQualities.STALE, "Stale");
        add(FSFoodQualities.BRUISED, "Bruised");
        add(FSFoodQualities.AGED, "Aged");
        add(FSFoodQualities.FLAT, "Flat");
        add(FSFoodQualities.MUSHY, "Mushy");
        add(FSFoodQualities.SPOILED, "Spoiled");
        add(FSFoodQualities.SOUR, "Sour");
        add(FSFoodQualities.WEAK, "Weak");
        add(FSFoodQualities.MOLDY, "Moldy");
        add(FSFoodQualities.RANCID, "Rancid");
        add(FSFoodQualities.DEAD, "Dead");
        add(FSFoodQualities.ROTTEN, "Rotten");

        addItem(FSItems.ROTTEN_MASS, "Rotten Mass");
        addItem(FSItems.DECOMPOSED_GOO, "Decomposed Goo");
    }

    public void add(ResourceKey<FoodQuality> key, String name) {
        String translationKey = key.registry().getPath() + "." + key.location().getNamespace() + "." + key.location().getPath();
        add(translationKey, name);
    }
}
