package n1zen.eterniamod.commands.skills.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.level.effects.CombatEffects;
import n1zen.eterniamod.skills.level.effects.MiningEffects;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static n1zen.eterniamod.Eterniamod.MOD_ID;
import static n1zen.eterniamod.commands.utils.CommandUtils.validateSkillType;

public class AddXp {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skillsAdmin")
                .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                .then(Commands.literal("addXp")
                        .then(Commands.argument("player", EntityArgument.player())
                                .suggests(CommandUtils::getCompletePlayers)
                                .then(Commands.argument("skill", StringArgumentType.word())
                                        .suggests(CommandUtils::getCompleteSkillTypes)
                                        .then(Commands.argument("amount", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    try {
                                                        ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                        String skillArgs =  StringArgumentType.getString(context, "skill");
                                                        double amount =  DoubleArgumentType.getDouble(context, "amount");
                                                        PlayerSkillXpState playerSkillXpState = PlayerSkillXpState.get(player.level());
                                                        PlayerSkillLevelState playerSkillLevelState = PlayerSkillLevelState.get(player.level());

                                                        UUID playerUUID = player.getUUID();
                                                        String playerName = player.getName().getString();

                                                        if(skillArgs.equals("all")) {
                                                            player.sendSystemMessage(
                                                                    Component.literal("+" + amount + " XP to all skills")
                                                            );
                                                            for(SkillType skillType :  SkillType.values()) {
                                                                double prevXp = playerSkillXpState.getSkillExp(playerUUID, skillType);
                                                                playerSkillXpState.addSkillExp(playerUUID, skillType, amount);
                                                                double xp = playerSkillXpState.getSkillExp(playerUUID, skillType);

                                                                player.sendSystemMessage(
                                                                        Component.literal(skillType.name() + " XP: " + prevXp + " -> " + xp)
                                                                );

                                                                getSkillLevel(skillType, playerSkillLevelState, playerUUID, xp, player);

                                                            }
                                                            String message = "Added " + amount + " XP to all skills of " + playerName;
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal(message),
                                                                    false
                                                            );

                                                            return 1;
                                                        }

                                                        SkillType skillType;

                                                        skillType = validateSkillType(context, skillArgs);
                                                        if (skillType == null) return 0;

                                                        double prevXp = playerSkillXpState.getSkillExp(playerUUID, skillType);
                                                        playerSkillXpState.addSkillExp(playerUUID, skillType, amount);
                                                        double xp = playerSkillXpState.getSkillExp(playerUUID, skillType);

                                                        String message = "Added " + amount + " XP to " + playerName + "'s " +  skillType.name() + " XP";
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal(message),
                                                                false
                                                        );
                                                        player.sendSystemMessage(
                                                                Component.literal("+" + amount + " " + skillType.name() + "XP")
                                                        );
                                                        player.sendSystemMessage(
                                                                Component.literal(skillType.name() + " XP: " + prevXp + " -> " + xp)
                                                        );

                                                        getSkillLevel(skillType, playerSkillLevelState, playerUUID, xp, player);


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

    private static void getSkillLevel(SkillType skillType, PlayerSkillLevelState playerSkillLevelState, UUID playerUUID, double xp, ServerPlayer player) {
        int prevSkillLevel = playerSkillLevelState.getSkillLevel(playerUUID, skillType);
        if (playerSkillLevelState.levelledUp(xp, playerUUID, skillType)) {
            player.sendSystemMessage(
                    Component.literal("Your " + skillType.name() + " Lvl has increased!")
            );
            int newSkillLevel = playerSkillLevelState.getSkillLevel(playerUUID, skillType);
            player.sendSystemMessage(
                    Component.literal(skillType.name() + " Lvl: " + prevSkillLevel + " -> " + newSkillLevel)
            );
        }

        CommandUtils.applySkillEffects(skillType, player, playerSkillLevelState, xp);
    }


}
