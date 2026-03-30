package com.joel4848.visibleghosts;

import com.joel4848.visibleghosts.command.ServerVisibleGhostsCommand;
import com.joel4848.visibleghosts.network.ServerNetworking;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import com.joel4848.visibleghosts.server.OverrideManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common/server-side mod initializer for Visible Ghosts.
 *
 * Registers:
 *  - The S2C packet payload type (must happen on both logical sides).
 *  - Player join/leave lifecycle events.
 *  - The /visibleghostsadmin and /vga server commands (OP level 1+).
 *
 * Does NOT register anything under /visibleghosts or /vg — those roots
 * are reserved for the client-side command dispatcher so that they work
 * even on vanilla servers.
 */
public class VisibleGhostsServer implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("visibleghosts-server");

    @Override
    public void onInitialize() {
        // Register the S2C payload type so Fabric can serialise override packets.
        ServerNetworking.register();

        // Send each player their current override state when they join.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            OverrideState state = OverrideManager.getOverride(handler.player.getUuid());
            ServerNetworking.sendOverrideTo(handler.player, state);
            LOGGER.debug("Sent override {} to {}", state, handler.player.getNameForScoreboard());
        });

        // Clean up the override entry when a player leaves.
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            OverrideManager.clearOverride(handler.player.getUuid());
            LOGGER.debug("Cleared override for {}", handler.player.getNameForScoreboard());
        });

        // Register /visibleghostsadmin and /vga (OP level 1+).
        ServerVisibleGhostsCommand.register();

        LOGGER.info("Visible Ghosts server-side initialised.");
    }
}
