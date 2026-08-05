package n1zen.eterniamod.commands.skills.level;

import com.mojang.brigadier.CommandDispatcher;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.showAllSkill;

public class ShowAllSkillLevelSelf {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .then(Commands.literal("level")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            PlayerSkillLevelState playerSkillLevelState = PlayerSkillLevelState.get(player.level());

                            showAllSkill(context, playerSkillLevelState, player);

                            return 1;
                        })
                )
        );
    }
}
