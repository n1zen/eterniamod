package n1zen.eterniamod.commands.skills;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import n1zen.eterniamod.commands.utils.CommandUtils;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.SkillType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.*;

public class ShowSpecificSkillExpSelf {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .then(Commands.argument("skill", StringArgumentType.word())
                        .suggests(CommandUtils::getCompleteSkillTypes)
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String skillArgs = StringArgumentType.getString(context, "skill");
                            PlayerSkillXpState playerSkillXpState = PlayerSkillXpState.get(player.level());

                            if(skillArgs.equals("all")) {
                                showAllSkillExp(context, playerSkillXpState, player);
                                return 1;
                            }

                            SkillType skillType;
                            skillType = validateSkillType(context, skillArgs);
                            if (skillType == null) return 0;

                            showSpecificSkillExp(context, playerSkillXpState, player, skillType);
                            return 1;
                        })
                )
        );
    }
}
