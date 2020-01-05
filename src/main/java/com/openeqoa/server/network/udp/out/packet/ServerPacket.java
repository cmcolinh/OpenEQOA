package com.openeqoa.server.network.udp.out.packet;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

@FunctionalInterface
public interface ServerPacket {
    /** Get the bytes to send */
    public byte[] getPacketBytes();

    /** The callback after this packet has been acknowledged */
    default void whenAcknowledged(MessageHandler messageHandler) {
        // default is to do nothing
    }

    @FunctionalInterface
    public static interface Builder {
        public ServerPacket build();
    }
}
