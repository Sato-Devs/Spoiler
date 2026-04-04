# Spoiler - Changes & Fixes

## New Food Quality States

Added 3 new food quality states to match the design doc:

| Quality | Saturation | Nutrition | Effects | Recipe Usage |
|---------|-----------|-----------|---------|--------------|
| **Flat** | 80% | 80% | None | All |
| **Weak** | 60% | 60% | None | All |
| **Dead** | 20% | 20% | None | None |

## New Food Stage Categories

Added 14 new food stage progressions (was 2, now 22 total):

| Category | Stages | Total Days |
|----------|--------|-----------|
| Seeds | Fresh > Stale > Weak > Dead > Rotten | 80 |
| Raw Meats | Fresh > Spoiled > Rancid > Rotten | 13 |
| Cooked Meats | Fresh > Stale > Spoiled > Rotten | 19 |
| Cooked Meals | Fresh > Stale > Spoiled > Rotten | 16 |
| Baked Goods | Fresh > Stale > Moldy > Rotten | 19 |
| Desserts | Fresh > Stale > Moldy > Rotten | 21 |
| Soup/Stew | Fresh > Stale > Sour > Rancid | 18 |
| Alcoholic Drinks | Fresh > Aged > Flat > Rancid | 170 |
| Drinks | Fresh > Stale > Sour > Rancid | 16 |
| Raw Eggs | Fresh > Spoiled > Rancid > Rotten | 17 |
| Cooked Eggs | Fresh > Stale > Spoiled > Rotten | 23 |
| Liquid Dairy | Fresh > Sour > Rancid > Rotten | 15 |
| Soft Dairy | Fresh > Stale > Moldy > Rancid > Rotten | 26 |
| Hard Dairy | Fresh > Stale > Moldy > Rancid > Rotten | 53 |
| Fat | Fresh > Stale > Moldy > Rancid > Rotten | 68 |
| Soft Produce | Fresh > Bruised > Mushy > Moldy > Rotten | 17 |
| Hard Produce | Fresh > Bruised > Mushy > Moldy > Rotten | 29 |
| Pickled Produce | Fresh > Aged > Sour > Rancid | 65 |
| Dry Produce | Fresh > Stale > Moldy > Rotten | 50 |
| Produce (generic) | Fresh > Bruised > Mushy > Moldy > Rotten | 21 |
| Default | Fresh > Stale > Spoiled > Moldy > Rotten | 23 |

## Per-Category `become_rotten_item` Flag

`FoodStages` now supports a `become_rotten_item` boolean field (defaults to `true`). When set to `false`, fully expired food stays in its last stage instead of transforming into Rotten Mass. Pickled produce uses this by default since pickled/fermented foods don't rot the same way.

Datapack authors can set this in their food stage JSONs:
```json
{
  "stages": [...],
  "become_rotten_item": false
}
```

## Salt Preservation System

- New `SaltPreservationRecipe`: combine any food item + any item tagged `spoiler:salts` in a crafting grid
- Salted foods spoil at **50%** of the normal rate
- Salted foods show a "Salted" label in gold on the tooltip
- Salt dot overlay texture renders on top of salted food items in inventory
- New item tag `spoiler:salts` - empty by default, add your mod's salt items to this tag
- New recipe JSON at `data/spoiler/recipes/salt_preservation.json`

## Composting

- Rotten Mass is now compostable (65% chance per use)
- Decomposed Goo is now compostable (85% chance per use)

Registered via `ComposterBlock.COMPOSTABLES` during `FMLCommonSetupEvent`.

## Item Tags (Vanilla Defaults)

All new categories are populated with appropriate vanilla items:

| Tag | Vanilla Items |
|-----|--------------|
| `unspoilable_foods` | Golden Apple, Enchanted Golden Apple, Golden Carrot |
| `raw_meats` | Porkchop, Beef, Chicken, Rabbit, Mutton |
| `cooked_meats` | Cooked Beef/Pork/Chicken/Rabbit/Mutton/Cod/Salmon |
| `cooked_meals` | Rabbit Stew |
| `baked_goods` | Bread, Pumpkin Pie |
| `desserts` | Cookie |
| `soup_stew` | Mushroom Stew, Beetroot Soup, Suspicious Stew |
| `drinks` | Honey Bottle |
| `raw_eggs` | Egg |
| `hard_produce` | Apple, Carrot, Potato, Beetroot |
| `soft_produce` | Sweet Berries, Glow Berries, Melon Slice, Chorus Fruit |
| `dry_produce` | Dried Kelp |
| `produce` | Apple, Sweet Berries, Glow Berries, Chorus Fruit, Melon Slice |
| `seeds` | Wheat/Beetroot/Melon/Pumpkin Seeds |
| `liquid_dairy` | Milk Bucket |
| `alcoholic_drinks` | *(empty - for mod compat)* |
| `cooked_eggs` | *(empty - for mod compat)* |
| `soft_dairy` | *(empty - for mod compat)* |
| `hard_dairy` | *(empty - for mod compat)* |
| `fat` | *(empty - for mod compat)* |
| `pickled_produce` | *(empty - for mod compat)* |
| `salts` | *(empty - for mod compat)* |

## Bug Fixes

### Critical: Nutrition/Saturation Modifiers Were Broken
- `nutritionMod` was `0.0` for all qualities, meaning eating ANY spoiled food gave **zero nutrition**
- `saturationMod` was `10.0` for all qualities, inflating saturation by 10x
- Fixed: all qualities now use proper 0.0-1.0 multipliers (e.g., Fresh = 1.0, Stale = 0.9, Rotten = 0.1)
- `FoodQuality.Builder.nutrition()` changed from `int` to `float` to support decimal multipliers

### Critical: Stage Calculation Was Wrong
- `getCurStage()` compared current day against individual stage `days` values as if they were cumulative thresholds
- But stage `days` represent the **duration** of each stage, not cumulative time
- Fixed: now properly accumulates days across stages to determine the correct current stage
- Added `getCurStageIndex()` helper for tooltip and internal use

### Critical: Food Effects Were Wrong Type
- All negative qualities used `HEALING` potion effect instead of `HUNGER`/`POISON`
- Fixed: Spoiled, Moldy, Rancid, Rotten now correctly apply Hunger and Poison effects
- Effects now have proper durations (200-1200 ticks) and amplifiers instead of instant/0

### Critical: MobEffectInstance Reuse Bug
- `FoodDataMixin` was applying the same `MobEffectInstance` object reference to players
- Since `MobEffectInstance` is mutable, this caused shared state between eaters
- Fixed: now creates a new `MobEffectInstance` copy for each application

### ConcurrentModificationException in Tick Sets
- `TICKING_BLOCK_ENTITIES` and `TICKING_ENTITIES` used `ObjectArraySet`
- These sets were modified (remove) during iteration in `processFoodSpoilage()`
- Fixed: switched to `ConcurrentHashMap.newKeySet()` which is safe for concurrent modification

### Config Options Not Respected
- `becomeRottenMass` and `becomeDecomposedGoo` config options existed but were never checked in spoilage logic
- Food always transformed to Rotten Mass/Decomposed Goo regardless of config
- Fixed: all transformation points now check these config values

### Temperature Mod Only Checked Cold Sweat
- `getTemperatureMod()` in `FSEvents` only checked for Cold Sweat, ignoring LSO
- Fixed: now checks both `hasColdSweat()` and `hasLSO()` for temperature-based spoilage

### Division by Zero in Recipe Utils
- `SpoilingRecipeUtils.setResultProgress()` divided by `foodItems` without checking for zero
- Could crash when no food items were in the crafting grid
- Fixed: added zero-check guard

### Food Combination Recipe Matched Single Items
- `FoodCombinationCrafting.matches()` returned true with only 1 food item
- This let players "combine" a single item into itself (duplication-adjacent behavior)
- Fixed: now requires at least 2 matching food items

### Freshness Calculation Edge Case
- `getFreshness()` could return negative values or divide by zero if `maxProgress <= 0`
- Fixed: returns 1.0 when maxProgress is invalid, clamps result to 0.0 minimum

### Null Safety in RecipeManagerMixin
- `getCurStage()` return value was used without null check, could NPE
- Fixed: added null guard with `continue`

### Client Null Safety
- `FSClientModEvents` accessed `Minecraft.getInstance().level` without null check
- Fixed: added null guard for level access

## Tooltip Improvements

- "Salted" indicator shown in gold when food is salt-preserved
- Advanced tooltip now shows saturation/nutrition as percentages (e.g., "90%") instead of raw modifier values
- Effect tooltips now show duration in seconds and probability percentage
- Effects section only shown when the quality actually has effects (no empty "Effects:" line)
- "Days until next stage" calculation fixed to use cumulative day tracking

## Datagen Updates

- `FSEnUSLangProvider` now generates translations for all 13 food qualities
- `FSTagsProvider` now includes `FSItemTagsProvider` for all item tags
- `FSBlockTagsProvider` added (required by Forge's item tag provider)
- `DataGatherer` updated to register all new providers
- All generated JSON files updated to match new data
