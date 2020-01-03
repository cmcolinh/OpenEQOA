package com.openeqoa.server.network.udp.out.packet;

@FunctionalInterface
public interface ServerPacket {
    /** Get the bytes to send */
    public byte[] getPacketBytes();

    @FunctionalInterface
    public static interface Builder {
        public ServerPacket build();
    }
}
