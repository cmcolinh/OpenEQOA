package com.openeqoa.server.network.udp.in.packet.message;

import static com.openeqoa.server.util.Log.println;

import java.net.InetAddress;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

import lombok.AllArgsConstructor;

/**
 * This class is a superclass for client messages that are implemented by
 * wrapping the raw packet bytes and specifying the startIndex and length of the
 * represented message
 * 
 * @author colin
 *
 */
@AllArgsConstructor
public abstract class AbstractPacketWrappingClientMessage implements ClientMessage {
    protected final InetAddress ipAddress;

    /** The bytes representing the packet that contained this message */
    protected final byte[] wrappedPacketBytes;

    /** The start index of this message in the packet */
    protected final int startIndex;

    /** The length of the message */
    protected final int length;

    @Override
    public short getClientId() {
        short clientId = wrappedPacketBytes[1];
        clientId = (short) ((clientId << 8) | wrappedPacketBytes[0]);

        return clientId;
    }

    @Override
    public short getServerId() {
        short serverId = wrappedPacketBytes[3];
        serverId = (short) ((serverId << 8) | wrappedPacketBytes[2]);

        return serverId;
    }

    public int getSessionId() {
        int sessionId = ((wrappedPacketBytes[10] & 0xff) << 24) | ((wrappedPacketBytes[9] & 0xff) << 16)
                | ((wrappedPacketBytes[8] & 0xff) << 8) | (wrappedPacketBytes[7] & 0xff);
        println(getClass(), Integer.toHexString(sessionId));
        println(getClass(), "hello");
        return sessionId;
    }

    @Override
    public abstract void accept(MessageHandler handler, ProcessPacket processPacket);
}
