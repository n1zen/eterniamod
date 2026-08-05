package n1zen.eterniamod.commands.skills.level;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.SkillType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.*;

public class ShowSpecificSkillLevelSelf {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .then(Commands.literal("level")
                        .then(Commands.argument("skill", StringArgumentType.word())
                                .suggests(CommandUtils::getCompleteSkillTypes)
                                .executes(context -> {
                                    ServerPlayer player = context.getSource().getPlayer();
                                    String skillArgs = StringArgumentType.getString(context, "skill");
                                    PlayerSkillLevelState playerSkillLevelState = PlayerSkillLevelState.get(player.level());

                                    if(skillArgs.equals("all")) {
                                        showAllSkill(context, playerSkillLevelState, player);
                                        return 1;
                                    }

                                    SkillType skillType;
                                    skillType = validateSkillType(context, skillArgs);
                                    if (skillType == null) return 0;

                                    showSpecificSkill(context, playerSkillLevelState, player, skillType);
                                    return 1;
                                })
                        )
                )
        );
    }
}
