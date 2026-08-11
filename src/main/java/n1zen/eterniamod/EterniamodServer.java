package n1zen.eterniamod;

import n1zen.eterniamod.commands.skills.exp.ShowAllSkillExpSelf;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpOther;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpSelf;
import n1zen.eterniamod.commands.skills.admin.AddXp;
import n1zen.eterniamod.commands.skills.admin.ClearXp;
import n1zen.eterniamod.commands.skills.level.ShowAllSkillLevelSelf;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelOther;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelSelf;
import n1zen.eterniamod.skills.PlayerSkillLevelState;
import n1zen.eterniamod.skills.PlayerSkillXpState;
import n1zen.eterniamod.skills.SkillType;
import n1zen.eterniamod.skills.xp.rewards.BlockXpReward;
import n1zen.eterniamod.skills.xp.rewards.BlockXpRewards;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static n1zen.eterniamod.Eterniamod.MOD_ID;

public class EterniamodServer implements DedicatedServerModInitializer {

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {

        ServerLifecycleEvents.SERVER_STARTING.register(server -> LOGGER.info("Eterniamod initializing..."));

        ServerLifecycleEvents.SERVER_STARTED.register(server -> LOGGER.info("Eterniamod has been initialized!"));

        PlayerBlockBreakEvents.AFTER.register((level, player, blockPos, blockState, block) -> {
            if (!level.isClientSide()) {
                BlockBreakLvlUp((ServerLevel) level, player, blockState);
            }

        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            // Show All Skills of Self
            ShowAllSkillExpSelf.register(dispatcher);
            ShowAllSkillLevelSelf.register(dispatcher);

            // Show Specific Skills of Self
            ShowSpecificSkillExpSelf.register(dispatcher);
            ShowSpecificSkillLevelSelf.register(dispatcher);

            // Show Specific Skills of Other Players
            ShowSpecificSkillExpOther.register(dispatcher);
            ShowSpecificSkillLevelOther.register(dispatcher);

            // Add XP to a player's skill (OP only)
            AddXp.register(dispatcher);

            // Clear skill xp of a player (OP only)
            ClearXp.register(dispatcher);

        });
    }

    private static void BlockBreakLvlUp(ServerLevel level, Player player, BlockState blockState) {
        Block brokenBlock = blockState.getBlock();
        BlockXpReward reward = BlockXpRewards.REWARDS.get(brokenBlock);

        if (reward != null) {
            double amount = reward.amount();
            SkillType skillType = reward.skill();

            UUID playerUUID = player.getUUID();

            PlayerSkillLevelState lvlState = PlayerSkillLevelState.get(level);
            PlayerSkillXpState xpState = PlayerSkillXpState.get(level);

            xpState.addSkillExp(playerUUID, skillType, amount);

            double xp = xpState.getSkillExp(playerUUID, skillType);

            if (lvlState.levelledUp(xp, playerUUID,  skillType)) {
                player.sendSystemMessage(
                        Component.literal(skillType.name() + " levelled up!")
                );
            }
        }
    }

}
