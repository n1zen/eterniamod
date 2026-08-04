package n1zen.eterniamod.commands.skills;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerLevelState;
import n1zen.eterniamod.skills.SkillType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.*;

public class ShowSpecificSkillsOther {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .then(Commands.argument("skill", StringArgumentType.word())
                        .suggests(CommandUtils::getCompleteSkills)
                                .then(Commands.argument("player", EntityArgument.player())
                                        .suggests(CommandUtils::getCompletePlayers)
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            PlayerLevelState playerLevelState = PlayerLevelState.get(player.level());
                                            String skillArgs =  StringArgumentType.getString(context, "skill");

                                            if(skillArgs.equals("all")) {
                                                showAllSkills(context, playerLevelState, player);
                                                return 1;
                                            }

                                            SkillType skillType;
                                            skillType = validateSkillType(context, skillArgs);
                                            if(skillType == null) return 0;

                                            showSpecificSkill(context, playerLevelState, player, skillType);
                                            return 1;
                                        })
                                )
                )
        );
    }
}
