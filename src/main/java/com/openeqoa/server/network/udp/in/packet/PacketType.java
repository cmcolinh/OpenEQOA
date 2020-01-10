package com.openeqoa.server.network.udp.in.packet;

public enum PacketType {
    UPDATE((byte) 0x20), CONFIRM((byte) 0x23), UPDATE_WITH_CONFIRM((byte) 0x63);

    private final byte value;

    private PacketType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
