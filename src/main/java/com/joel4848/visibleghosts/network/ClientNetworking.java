package com.joel4848.visibleghosts.network;

import com.joel4848.visibleghosts.VisibleGhostsClient;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverridePayload;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Handles all client-side networking for Visible Ghosts.
 *
 * The server pushes an {@link OverrideState} to this client whenever it changes.
 * The mixins read {@link #getCurrentOverride()} each frame to decide whether to
 * render invisible players.
 *
 * When the client is on a vanilla server (no mod) the override stays at
 * {@link OverrideState#NONE} and the player's own config setting is used.
 * The override is reset to NONE on disconnect, handled in
 * {@link VisibleGhostsClient}.
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
     * Called when the player disconnects so a stale override from a previous
     * server session is never carried over to the next.
     */
    public static void reset() {
        currentOverride = OverrideState.NONE;
    }

    private ClientNetworking() {}
}
