package n1zen.eterniamod.commands.skills;

import com.mojang.brigadier.CommandDispatcher;
import n1zen.eterniamod.skills.PlayerLevelState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.showAllSkills;

public class ShowAllSkillsSelf {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    PlayerLevelState playerLevelState = PlayerLevelState.get(player.level());

                    showAllSkills(context, playerLevelState, player);

                    return 1;
                })
        );
    }
}