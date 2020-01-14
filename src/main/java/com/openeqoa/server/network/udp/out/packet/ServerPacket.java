package com.openeqoa.server.network.udp.out.packet;

import java.util.Collections;
import java.util.List;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.packet.message.ServerMessage;

@FunctionalInterface
public interface ServerPacket {
    /** Get the bytes to send */
    byte[] getPacketBytes();

    /** The callback after this packet has been acknowledged */
    default void whenAcknowledged(MessageHandler messageHandler) {
        // default is to do nothing
    }

    @FunctionalInterface
    public static interface Builder {
    	/** get a list of reliable messages associated with this packet */
    	default List<ServerMessage.Builder> reliableMessageBuilders() {
    		return Collections.emptyList();
    	};

    	/** Build the server packet */
        ServerPacket build();
    }
}
