package n1zen.eterniamod.commands.skills.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.SkillType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static n1zen.eterniamod.Eterniamod.MOD_ID;
import static n1zen.eterniamod.commands.utils.CommandUtils.validateSkillType;

public class AddLvl {
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skillsAdmin")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.literal("addLvl")
                        .then(Commands.argument("player", EntityArgument.player())
                                .suggests(CommandUtils::getCompletePlayers)
                                .then(Commands.argument("skill", StringArgumentType.word())
                                        .suggests(CommandUtils::getCompleteSkillTypes)
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> {
                                                    try {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        String skillArgs =  StringArgumentType.getString(context, "skill");
                                                        int amount =  IntegerArgumentType.getInteger(context, "amount");
                                                        PlayerSkillLevelState playerSkillLevelState = PlayerSkillLevelState.get(player.level());

                                                        UUID playerUUID = player.getUUID();
                                                        String playerName = player.getName().getString();

                                                        if(skillArgs.equals("all")) {
                                                            player.sendSystemMessage(
                                                                    Component.literal("+" + amount + " Lvl to all skills")
                                                            );
                                                            for(SkillType skillType :  SkillType.values()) {
                                                                int prevLvl = playerSkillLevelState.getSkillLevel(playerUUID, skillType);
                                                                playerSkillLevelState.addSkillLevel(playerUUID, skillType, amount);
                                                                int lvl = playerSkillLevelState.getSkillLevel(playerUUID, skillType);

                                                                player.sendSystemMessage(
                                                                        Component.literal(skillType.name() + " Lvl: " + prevLvl + " -> " + lvl)
                                                                );
                                                            }
                                                            String message = "Added " + amount + " Lvl to all skills of " + playerName;
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(message),
                                                                    false
                                                            );
                                                            return 1;
                                                        }

                                                        SkillType skillType;

                                                        skillType = validateSkillType(context, skillArgs);
                                                        if (skillType == null) return 0;

                                                        int prevLvl = playerSkillLevelState.getSkillLevel(playerUUID, skillType);
                                                        playerSkillLevelState.addSkillLevel(playerUUID, skillType, amount);
                                                        int lvl = playerSkillLevelState.getSkillLevel(playerUUID, skillType);

                                                        String message = "Added " + amount + " Lvl to " + playerName + "'s " +  skillType.name() + " XP";
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal(message),
                                                                false
                                                        );
                                                        player.sendSystemMessage(
                                                                Component.literal("+" + amount + " " + skillType.name() + "XP")
                                                        );
                                                        player.sendSystemMessage(
                                                                Component.literal(skillType.name() + " XP: " + prevLvl + " -> " + lvl)
                                                        );
                                                        return 1;

                                                    } catch (Exception e) {
                                                        LOGGER.error("skillsadmin command failed: ", e);
                                                        context.getSource().sendFailure(
                                                                Component.literal("Error: " + e.getMessage())
                                                        );
                                                        return 0;
                                                    }
                                                })
                                        )
                                )
                        )
                )
        );
    }
}
