package com.joel4848.visibleghosts;

import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VisibleGhostsClient implements ClientModInitializer {
    public static final String MOD_ID = "visibleghosts";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        // Load config
        ModConfig.getInstance();

        // Register client-side packet receivers (override state + config updates)
        ClientNetworking.register();

        // Reset the server override on disconnect so a stale override from a
        // previous session is never carried over to the next server.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientNetworking.reset();
            LOGGER.debug("Disconnected – server override reset to NONE.");
        });

        LOGGER.info("Visible Ghosts client loaded!");
    }
}