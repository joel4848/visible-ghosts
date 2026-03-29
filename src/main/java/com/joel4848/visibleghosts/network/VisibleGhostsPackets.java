package com.joel4848.visibleghosts.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/**
 * Defines the custom network packet used to push a player's server-side
 * override state down to that player's client.
 *
 * Packet flow:
 *   SERVER -> CLIENT  (OverridePayload)
 *     Sent when:
 *       - a player joins a server that has the mod installed, or
 *       - an operator changes that player's override via the server command.
 *     Content: the OverrideState that applies to the receiving client.
 */
public final class VisibleGhostsPackets {

    /** The three possible server-set states for a given player. */
    public enum OverrideState {
        /** Server forces invisible players to be visible (transparent) for this client. */
        VISIBLE,
        /** Server forces invisible players to be completely hidden for this client. */
        INVISIBLE,
        /** No server override; the client's own mod setting takes effect.
         *  Also used when the server does not have the mod installed at all. */
        NONE;

        public static OverrideState fromByte(byte b) {
            return switch (b) {
                case 0 -> VISIBLE;
                case 1 -> INVISIBLE;
                default -> NONE;
            };
        }

        public byte toByte() {
            return switch (this) {
                case VISIBLE   -> (byte) 0;
                case INVISIBLE -> (byte) 1;
                case NONE      -> (byte) 2;
            };
        }
    }

    // ------------------------------------------------------------------
    // Packet: server -> client  (override state for the receiving player)
    // ------------------------------------------------------------------

    public static final Identifier OVERRIDE_PACKET_ID =
            Identifier.of("visibleghosts", "override_state");

    /**
     * Payload record carrying the server-assigned OverrideState.
     * Sent only to the player whose override has changed.
     */
    public record OverridePayload(OverrideState state) implements CustomPayload {

        public static final CustomPayload.Id<OverridePayload> ID =
                new CustomPayload.Id<>(OVERRIDE_PACKET_ID);

        /** Codec used by Fabric's network layer to encode/decode this payload. */
        public static final PacketCodec<PacketByteBuf, OverridePayload> CODEC =
                PacketCodec.of(
                        (payload, buf) -> buf.writeByte(payload.state().toByte()),
                        buf -> new OverridePayload(OverrideState.fromByte(buf.readByte()))
                );

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    private VisibleGhostsPackets() {}
}
