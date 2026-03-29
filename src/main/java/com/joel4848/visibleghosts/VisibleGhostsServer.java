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
 * This runs on the logical server regardless of whether a dedicated server or
 * an integrated (singleplayer) server is in use.
 *
 * Responsibilities:
 *  - Register the S2C packet type (must happen on both sides).
 *  - Register server-side lifecycle events (player join / leave).
 *  - Register the /visibleghosts overrideVisibility command.
 *
 * The mod is optional on the server: a client with the mod can connect to a
 * vanilla server and everything still works (no packets are sent, so the
 * override stays NONE and the client's local config takes full effect).
 */
public class VisibleGhostsServer implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("visibleghosts-server");

    @Override
    public void onInitialize() {
        // Register the S2C payload type. This must be done on the server side so
        // that Fabric's network layer knows how to serialise the packet.
        ServerNetworking.register();

        // When a player joins, send them their current override immediately.
        // This handles the case where an override was set while the player was
        // offline (currently impossible since overrides are in-memory only, but
        // future-proofing in case persistence is added later).
        // More practically, it re-sends the override to a player who rejoins
        // without a server restart.
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            OverrideState state = OverrideManager.getOverride(handler.player.getUuid());
            // Always send on join so the client is in sync.
            ServerNetworking.sendOverrideTo(handler.player, state);
            LOGGER.debug("Sent override {} to {}", state, handler.player.getNameForScoreboard());
        });

        // When a player leaves, clean up the override entry to keep memory lean.
        // (The override state is NOT persisted across sessions intentionally.)
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            OverrideManager.clearOverride(handler.player.getUuid());
            LOGGER.debug("Cleared override for {}", handler.player.getNameForScoreboard());
        });

        // Register the /visibleghosts overrideVisibility command.
        ServerVisibleGhostsCommand.register();

        LOGGER.info("Visible Ghosts server-side initialised.");
    }
}
