package com.joel4848.visibleghosts.network;

import com.joel4848.visibleghosts.VisibleGhostsClient;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverridePayload;
import com.joel4848.visibleghosts.network.VisibleGhostsPackets.OverrideState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class ClientNetworking {

    private static OverrideState currentOverride = OverrideState.NONE;

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

    public static OverrideState getCurrentOverride() {
        return currentOverride;
    }

    public static void reset() {
        currentOverride = OverrideState.NONE;
    }

    private ClientNetworking() {}
}
