package com.thepigcat.spoiler.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.thepigcat.spoiler.FSRegistries;
import com.thepigcat.spoiler.api.FoodQuality;
import com.thepigcat.spoiler.api.FoodStage;
import com.thepigcat.spoiler.api.FoodStages;
import com.thepigcat.spoiler.utils.NBTSpoilingUtils;
import com.thepigcat.spoiler.utils.SpoilingUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class SpoilerCommand {

    private static final SuggestionProvider<CommandSourceStack> STAGE_SUGGESTIONS = (ctx, builder) -> {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player != null) {
            ItemStack stack = player.getMainHandItem();
            if (NBTSpoilingUtils.hasFoodState(stack)) {
                FoodStages stages = SpoilingUtils.getStages(stack, ctx.getSource().registryAccess());
                if (stages != null) {
                    List<String> names = new ArrayList<>();
                    for (FoodStage stage : stages.stages()) {
                        names.add(stage.quality().location().getPath());
                    }
                    return SharedSuggestionProvider.suggest(names, builder);
                }
            }
        }
        List<String> all = new ArrayList<>();
        for (Holder.Reference<FoodQuality> ref : ctx.getSource().registryAccess().lookupOrThrow(FSRegistries.FOOD_QUALITY_KEY).listElements().toList()) {
            all.add(ref.key().location().getPath());
        }
        return SharedSuggestionProvider.suggest(all, builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("spoiler").requires(s -> s.hasPermission(2))
                .then(Commands.literal("advance")
                        .then(Commands.argument("days", IntegerArgumentType.integer(1))
                                .executes(ctx -> advance(ctx.getSource(), IntegerArgumentType.getInteger(ctx, "days")))))
                .then(Commands.literal("set_stage")
                        .then(Commands.argument("stage", StringArgumentType.word())
                                .suggests(STAGE_SUGGESTIONS)
                                .executes(ctx -> setStage(ctx.getSource(), StringArgumentType.getString(ctx, "stage")))))
                .then(Commands.literal("set_progress")
                        .then(Commands.argument("ticks", FloatArgumentType.floatArg(0))
                                .executes(ctx -> setProgress(ctx.getSource(), FloatArgumentType.getFloat(ctx, "ticks")))))
                .then(Commands.literal("info")
                        .executes(ctx -> info(ctx.getSource())))
                .then(Commands.literal("salt")
                        .executes(ctx -> salt(ctx.getSource())))
                .then(Commands.literal("reset")
                        .executes(ctx -> reset(ctx.getSource())))
                .then(Commands.literal("max")
                        .executes(ctx -> max(ctx.getSource())))
        );
    }

    private static int advance(CommandSourceStack source, int days) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!NBTSpoilingUtils.hasFoodState(stack)) {
            source.sendFailure(Component.literal("Held item has no food state"));
            return 0;
        }
        float current = NBTSpoilingUtils.getSpoilingProgress(stack);
        NBTSpoilingUtils.setSpoilingProgress(stack, current + (days * 24000f));
        source.sendSuccess(() -> Component.literal("Advanced " + days + " days (+" + (days * 24000) + " ticks)"), true);
        return 1;
    }

    private static int setStage(CommandSourceStack source, String stageName) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!NBTSpoilingUtils.hasFoodState(stack)) {
            source.sendFailure(Component.literal("Held item has no food state"));
            return 0;
        }
        FoodStages stages = SpoilingUtils.getStages(stack, source.registryAccess());
        if (stages == null) {
            source.sendFailure(Component.literal("No food stages found"));
            return 0;
        }
        List<FoodStage> stageList = stages.stages();
        int targetIndex = -1;
        for (int i = 0; i < stageList.size(); i++) {
            if (stageList.get(i).quality().location().getPath().equals(stageName)) {
                targetIndex = i;
                break;
            }
        }
        if (targetIndex == -1) {
            source.sendFailure(Component.literal("Stage '" + stageName + "' not found on this item"));
            return 0;
        }
        int cumulativeDays = 0;
        for (int i = 0; i < targetIndex; i++) {
            cumulativeDays += stageList.get(i).days();
        }
        float targetProgress = cumulativeDays * 24000f;
        NBTSpoilingUtils.setSpoilingProgress(stack, targetProgress);
        int idx = targetIndex;
        source.sendSuccess(() -> Component.literal("Set to stage " + idx + " (" + stageName + ")"), true);
        return 1;
    }

    private static int setProgress(CommandSourceStack source, float ticks) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!NBTSpoilingUtils.hasFoodState(stack)) {
            source.sendFailure(Component.literal("Held item has no food state"));
            return 0;
        }
        NBTSpoilingUtils.setSpoilingProgress(stack, ticks);
        source.sendSuccess(() -> Component.literal("Set progress to " + ticks + " ticks"), true);
        return 1;
    }

    private static int info(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!NBTSpoilingUtils.hasFoodState(stack)) {
            source.sendFailure(Component.literal("Held item has no food state"));
            return 0;
        }

        float progress = NBTSpoilingUtils.getSpoilingProgress(stack);
        float maxProgress = NBTSpoilingUtils.getMaxSpoilingProgress(stack);
        float modifier = NBTSpoilingUtils.getSpoilingModifier(stack);
        long creation = NBTSpoilingUtils.getCreationTime(stack);
        long lastDay = NBTSpoilingUtils.getLastDayTime(stack);
        boolean salted = SpoilingUtils.isSalted(stack);

        FoodStages stages = SpoilingUtils.getStages(stack, source.registryAccess());
        FoodStage curStage = SpoilingUtils.getCurStage(stack, source.registryAccess());
        int curIndex = SpoilingUtils.getCurStageIndex(stack, source.registryAccess());

        source.sendSuccess(() -> Component.literal("--- Spoiler Info ---").withStyle(ChatFormatting.GOLD), false);
        source.sendSuccess(() -> Component.literal("Progress: " + String.format("%.0f / %.0f", progress, maxProgress) + " ticks (" + String.format("%.1f / %.1f", progress / 24000f, maxProgress / 24000f) + " days)"), false);
        source.sendSuccess(() -> Component.literal("Freshness: " + Math.round(SpoilingUtils.getFreshness(progress, maxProgress) * 100) + "%"), false);
        source.sendSuccess(() -> Component.literal("Modifier: " + modifier + (salted ? " (salted, effective x0.5)" : "")), false);
        source.sendSuccess(() -> Component.literal("Creation: " + creation + " | Last update: " + lastDay), false);

        if (curStage != null) {
            String qualityName = curStage.quality().location().getPath();
            source.sendSuccess(() -> Component.literal("Current stage: " + curIndex + " (" + qualityName + ")"), false);
        }

        if (stages != null) {
            ResourceKey<FoodStages> stagesKey = NBTSpoilingUtils.getFoodStages(stack);
            String categoryName = stagesKey != null ? stagesKey.location().getPath() : "unknown";
            source.sendSuccess(() -> Component.literal("Category: " + categoryName + " | Become rotten: " + stages.becomeRottenItem()), false);
            StringBuilder sb = new StringBuilder("Stages: ");
            for (int i = 0; i < stages.stages().size(); i++) {
                FoodStage s = stages.stages().get(i);
                if (i > 0) sb.append(" > ");
                String name = s.quality().location().getPath();
                sb.append(name).append("(").append(s.days()).append("d)");
                if (i == curIndex) sb.append("*");
            }
            String stagesStr = sb.toString();
            source.sendSuccess(() -> Component.literal(stagesStr), false);
        }
        return 1;
    }

    private static int salt(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            source.sendFailure(Component.literal("No item in hand"));
            return 0;
        }
        boolean wasSalted = SpoilingUtils.isSalted(stack);
        SpoilingUtils.setSalted(stack, !wasSalted);
        source.sendSuccess(() -> Component.literal(wasSalted ? "Removed salt" : "Applied salt"), true);
        return 1;
    }

    private static int reset(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!stack.isEdible()) {
            source.sendFailure(Component.literal("Held item is not food"));
            return 0;
        }
        SpoilingUtils.initialize(stack, source.getLevel().dayTime(), 1f, source.registryAccess());
        source.sendSuccess(() -> Component.literal("Reset to fresh"), true);
        return 1;
    }

    private static int max(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        if (!NBTSpoilingUtils.hasFoodState(stack)) {
            source.sendFailure(Component.literal("Held item has no food state"));
            return 0;
        }
        float maxProgress = NBTSpoilingUtils.getMaxSpoilingProgress(stack);
        NBTSpoilingUtils.setSpoilingProgress(stack, maxProgress);
        source.sendSuccess(() -> Component.literal("Maxed out spoilage - will convert on next tick"), true);
        return 1;
    }
}
