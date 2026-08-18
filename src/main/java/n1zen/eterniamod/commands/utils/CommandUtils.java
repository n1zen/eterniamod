package n1zen.eterniamod.commands.utils;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.level.effects.CombatEffects;
import n1zen.eterniamod.skills.level.effects.MiningEffects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CommandUtils {

    public static void showAllSkill(CommandContext<CommandSourceStack> context, PlayerSkillXpState playerSkillXpState, ServerPlayer player) {
        for(SkillType skillType : SkillType.values()) {
            double xp = playerSkillXpState.getSkillExp(player.getUUID(), skillType);
            String message = skillType.name() +" XP: " + xp;
            context.getSource().sendSuccess(
                    () -> Component.literal(message),
                    false
            );
        }
    }

    public static void showAllSkill(CommandContext<CommandSourceStack> context, PlayerSkillLevelState playerSkillLevelState, ServerPlayer player) {
        for(SkillType skillType : SkillType.values()) {
            int lvl = playerSkillLevelState.getSkillLevel(player.getUUID(), skillType);
            String message = skillType.name() +" Lvl: " + lvl;
            context.getSource().sendSuccess(
                    () -> Component.literal(message),
                    false
            );
        }
    }

    public static void showSpecificSkill(CommandContext<CommandSourceStack> context, PlayerSkillXpState playerSkillXpState, ServerPlayer player, SkillType skillType) {
        double xp =  playerSkillXpState.getSkillExp(player.getUUID(), skillType);
        String message = skillType.name() + " XP: " + xp;
        context.getSource().sendSuccess(
                () -> Component.literal(message),
                false
        );
    }

    public static void showSpecificSkill(CommandContext<CommandSourceStack> context, PlayerSkillLevelState playerSkillLevelState, ServerPlayer player, SkillType skillType) {
        int lvl =  playerSkillLevelState.getSkillLevel(player.getUUID(), skillType);
        String message = skillType.name() + " Lvl: " + lvl;
        context.getSource().sendSuccess(
                () -> Component.literal(message),
                false
        );
    }

    public static @Nullable SkillType validateSkillType(CommandContext<CommandSourceStack> context, String skillArgs) {
        SkillType skillType;
        try {
            skillType =  SkillType.valueOf(skillArgs.toUpperCase());
        } catch (IllegalArgumentException e) {
            context.getSource().sendFailure(
                    Component.literal("Unknown skill")
            );
            return null;
        }
        return skillType;
    }

    public static CompletableFuture<Suggestions> getCompletePlayers(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        ServerLevel level = context.getSource().getLevel();
        for(ServerPlayer player : level.players()) {
            builder.suggest(player.getName().getString());
        }
        return builder.buildFuture();
    }

    public static CompletableFuture<Suggestions> getCompleteSkillTypes(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        builder.suggest("all");
        for(SkillType skillType : SkillType.values()) {
            builder.suggest(skillType.name().toLowerCase());
        }
        return builder.buildFuture();
    }

    public static void applySkillEffects(SkillType skillType, ServerPlayer player, PlayerSkillLevelState levelState, double amount) {
        switch (skillType) {
            case MINING:
                MiningEffects.applyMiningSpeedBonus(player, levelState.getLvlForExp(amount));
                break;
            case COMBAT:
                CombatEffects.applyAttackDamageModifier(player, levelState.getLvlForExp(amount));
                break;
            default:
                break;
        }
    }
}
