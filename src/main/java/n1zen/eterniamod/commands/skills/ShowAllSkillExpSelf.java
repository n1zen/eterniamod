package n1zen.eterniamod.commands.skills;

import com.mojang.brigadier.CommandDispatcher;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import static n1zen.eterniamod.commands.utils.CommandUtils.showAllSkillExp;

public class ShowAllSkillExpSelf {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("skills")
                .then(Commands.literal("exp")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            PlayerSkillXpState playerSkillXpState = PlayerSkillXpState.get(player.level());

                            showAllSkillExp(context, playerSkillXpState, player);

                            return 1;
                        })
                )
        );
    }
}