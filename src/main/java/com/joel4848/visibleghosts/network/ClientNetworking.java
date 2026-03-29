package com.joel4848.visibleghosts.network;

import com.joel4848.visibleghosts.VisibleGhostsClient;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverridePayload;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Handles all client-side networking for Visible Ghosts.
 *
 * The server pushes an {@link OverrideState} to this client whenever it changes.
 * The mixin reads {@link #getCurrentOverride()} each frame to decide whether to
 * render invisible players.
 *
 * When the client is on a vanilla server (no mod) the override stays at
 * {@link OverrideState#NONE} and the player's own config setting is used.
 * The override is also reset to NONE when the player disconnects, which is
 * handled in {@link VisibleGhostsClient}.
 */
public final class ClientNetworking {

    private static OverrideState currentOverride = OverrideState.NONE;

    /**
     * Registers the client-side receiver for the override packet.
     * Must be called from the client mod initializer.
     */
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                OverridePayload.ID,
                (payload, context) -> {
                    currentOverride = payload.state();
                    VisibleGhostsClient.LOGGER.info(
                            "Received server override: {}", currentOverride);
                }
        );
    }

    /** Returns the override state most recently received from the server. */
    public static OverrideState getCurrentOverride() {
        return currentOverride;
    }

    /**
     * Resets the override to NONE.
     * Should be called when the player disconnects from a server so that
     * a subsequent connection to a vanilla server doesn't keep a stale override.
     */
    public static void reset() {
        currentOverride = OverrideState.NONE;
    }

    private ClientNetworking() {}
}
