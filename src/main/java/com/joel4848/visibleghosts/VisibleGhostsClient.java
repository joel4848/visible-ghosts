package com.joel4848.visibleghosts;

import com.joel4848.visibleghosts.command.VisibleGhostsCommand;
import com.joel4848.visibleghosts.config.ModConfig;
import com.joel4848.visibleghosts.network.ClientNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
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

        // Register the client-side packet receiver for server override packets.
        ClientNetworking.register();

        // Reset the server override when the player disconnects so a stale
        // override from a previous server session is never carried over.
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientNetworking.reset();
            LOGGER.debug("Disconnected - server override reset to NONE.");
        });

        // Register client-side commands (/visibleghosts and /vg).
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                VisibleGhostsCommand.register(dispatcher));

        LOGGER.info("Visible Ghosts mod loaded!");
    }
}
