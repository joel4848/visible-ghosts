package com.joel4848.visibleghosts.server;

import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the server-assigned {@link OverrideState} for each online player.
 *
 * Purely in-memory: overrides do NOT persist across server restarts.
 * The default for every player is {@link OverrideState#NONE}.
 */
public final class OverrideManager {

    private static final Map<UUID, OverrideState> overrides = new ConcurrentHashMap<>();

    /**
     * Returns the current override for the given player UUID.
     * Returns {@link OverrideState#NONE} if no override has been set.
     */
    public static OverrideState getOverride(UUID playerUuid) {
        return overrides.getOrDefault(playerUuid, OverrideState.NONE);
    }

    /**
     * Sets (or clears, if {@code state} is {@link OverrideState#NONE}) the
     * override for the given player UUID.
     */
    public static void setOverride(UUID playerUuid, OverrideState state) {
        if (state == OverrideState.NONE) {
            overrides.remove(playerUuid);
        } else {
            overrides.put(playerUuid, state);
        }
    }

    /**
     * Removes any override entry for the given player.
     * Called when a player disconnects so the map doesn't grow unboundedly.
     */
    public static void clearOverride(UUID playerUuid) {
        overrides.remove(playerUuid);
    }

    private OverrideManager() {}
}
