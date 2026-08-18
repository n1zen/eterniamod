package n1zen.eterniamod.commands.skills.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.level.effects.MiningEffects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;

import java.util.UUID;

import static n1zen.eterniamod.commands.utils.CommandUtils.validateSkillType;

public class ClearXp {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skillsAdmin")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.literal("clearXp")
                        .then(Commands.argument("player", EntityArgument.player())
                                .suggests(CommandUtils::getCompletePlayers)
                                .then(Commands.argument("skill", StringArgumentType.word())
                                        .suggests(CommandUtils::getCompleteSkillTypes)
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            PlayerSkillXpState xpState = PlayerSkillXpState.get(player.level());
                                            PlayerSkillLevelState levelState = PlayerSkillLevelState.get(player.level());
                                            String skillArgs = StringArgumentType.getString(context, "skill");

                                            UUID playerUUID = player.getUUID();
                                            String playerName = player.getName().getString();

                                            if (skillArgs.equals("all")) {
                                                for (SkillType skillType : SkillType.values()) {
                                                    xpState.removeSkillExp(playerUUID, skillType);
                                                    levelState.removeSkillLevel(playerUUID, skillType);
                                                    if(skillType.equals(SkillType.MINING)) {
                                                        MiningEffects.applyMiningSpeedBonus(player, levelState.getLvlForExp(0.0));
                                                    }
                                                }
                                                String message = "Cleared all of " + playerName + "'s skill xp and levels";
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal(message),
                                                        false
                                                );
                                                player.sendSystemMessage(Component.literal("Your skills' xp and levels have been cleared"));

                                                return 1;
                                            }

                                            SkillType skillType = validateSkillType(context, skillArgs);
                                            if (skillType == null) return 0;

                                            xpState.removeSkillExp(playerUUID, skillType);
                                            levelState.removeSkillLevel(playerUUID, skillType);

                                            if(skillType.equals(SkillType.MINING)) {
                                                MiningEffects.applyMiningSpeedBonus(player, levelState.getLvlForExp(0.0));
                                            }

                                            String message = "Cleared " + playerName + "'s " + skillType.name() + " xp and level";
                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(message),
                                                    false
                                            );
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }
}