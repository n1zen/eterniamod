package n1zen.eterniamod;

import n1zen.eterniamod.commands.skills.admin.AddLvl;
import n1zen.eterniamod.commands.skills.admin.ClearLvl;
import n1zen.eterniamod.commands.skills.exp.ShowAllSkillExpSelf;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpOther;
import n1zen.eterniamod.commands.skills.exp.ShowSpecificSkillExpSelf;
import n1zen.eterniamod.commands.skills.admin.AddXp;
import n1zen.eterniamod.commands.skills.admin.ClearXp;
import n1zen.eterniamod.commands.skills.level.ShowAllSkillLevelSelf;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelOther;
import n1zen.eterniamod.commands.skills.level.ShowSpecificSkillLevelSelf;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            // Add Lvls to a player's skill (OP only)
            AddLvl.register(dispatcher);

            // Clear skill xp of a player (OP only)
            ClearXp.register(dispatcher);
            // Clear skill lvl of a player (OP only)
            ClearLvl.register(dispatcher);

        });
    }

}
