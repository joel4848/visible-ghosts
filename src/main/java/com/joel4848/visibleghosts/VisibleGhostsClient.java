package com.joel4848.visibleghosts;

import com.joel4848.visibleghosts.command.VisibleGhostsCommand;
import com.joel4848.visibleghosts.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisibleGhostsClient implements ClientModInitializer {
    public static final String MOD_ID = "visibleghosts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // Load config
        ModConfig.getInstance();

        // Register commands
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            VisibleGhostsCommand.register(dispatcher);
        });

        LOGGER.info("Visible Ghosts mod loaded!");
    }
}