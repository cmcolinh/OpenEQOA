package com.openeqoa.server.network.udp.out.packet;

@FunctionalInterface
public interface ServerPacket {
    /** Get the bytes to send */
    public byte[] getPacketBytes();

    /** The callback after this packet has been acknowledged */
    default void whenAcknowledged() {
        // default is to do nothing
    }

    @FunctionalInterface
    public static interface Builder {
        public ServerPacket build();
    }
}
