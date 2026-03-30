package com.joel4848.visibleghosts.network;

import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverridePayload;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Handles all server-side networking for Visible Ghosts.
 *
 * Call {@link #register()} once during server initialisation.
 * Then use {@link #sendOverrideTo(ServerPlayerEntity, OverrideState)} whenever
 * a player's override state needs to be (re)sent to their client.
 */
public final class ServerNetworking {

    /**
     * Registers the S2C payload type so Fabric knows how to serialise it.
     * Safe to call from the common/server initializer.
     */
    public static void register() {
        PayloadTypeRegistry.playS2C().register(
                OverridePayload.ID,
                OverridePayload.CODEC
        );
    }

    /**
     * Sends the given {@link OverrideState} to a specific player's client.
     * If the client does not have the mod installed the packet is silently
     * ignored on the other end.
     */
    public static void sendOverrideTo(ServerPlayerEntity player, OverrideState state) {
        ServerPlayNetworking.send(player, new OverridePayload(state));
    }

    private ServerNetworking() {}
}
