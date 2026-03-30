package com.joel4848.visibleghosts.network;

import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverridePayload;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;

public final class ServerNetworking {

    public static void register() {
        PayloadTypeRegistry.playS2C().register(
                OverridePayload.ID,
                OverridePayload.CODEC
        );
    }

    public static void sendOverrideTo(ServerPlayerEntity player, OverrideState state) {
        ServerPlayNetworking.send(player, new OverridePayload(state));
    }

    private ServerNetworking() {}
}
