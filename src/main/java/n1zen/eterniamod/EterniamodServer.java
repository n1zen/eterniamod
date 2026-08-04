package n1zen.eterniamod;

import n1zen.eterniamod.commands.skills.ShowAllSkillsSelf;
import n1zen.eterniamod.commands.skills.ShowSpecificSkillsOther;
import n1zen.eterniamod.commands.skills.ShowSpecificSkillsSelf;
import n1zen.eterniamod.commands.skills.admin.AddXp;
import n1zen.eterniamod.commands.skills.admin.ClearXp;
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

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LOGGER.info("Eterniamod initializing...");
        });

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LOGGER.info("Eterniamod has been initialized!");
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {

            // Show All Skills of Self
            ShowAllSkillsSelf.register(dispatcher);

            // Show Specific Skills of Self
            ShowSpecificSkillsSelf.register(dispatcher);

            // Show Specific Skills of Other Players
            ShowSpecificSkillsOther.register(dispatcher);

            // Add XP to a player's skill (OP only)
            AddXp.register(dispatcher);

            // Clear skill xp of a player (OP only)
            ClearXp.register(dispatcher);
        });
    }

}
