package com.joel4848.visibleghosts.network;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public final class VisibleGhostsPackets {

    public enum OverrideState {
        VISIBLE,

        INVISIBLE,

        NONE;

        public static OverrideState fromByte(byte b) {
            return switch (b) {
                case 0  -> VISIBLE;
                case 1  -> INVISIBLE;
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

    public record OverridePayload(OverrideState state) implements CustomPayload {

        public static final CustomPayload.Id<OverridePayload> ID =
                new CustomPayload.Id<>(OVERRIDE_PACKET_ID);

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
