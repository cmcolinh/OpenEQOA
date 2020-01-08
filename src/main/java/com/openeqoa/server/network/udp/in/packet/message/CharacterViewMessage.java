package com.openeqoa.server.network.udp.in.packet.message;

import java.net.InetAddress;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x002C
 * 
 * @author colin
 *
 */
public final class CharacterViewMessage extends AbstractPacketWrappingClientMessage {
    public CharacterViewMessage(InetAddress ipAddress, byte[] wrappedPacketBytes, int startIndex, int length) {
        super(ipAddress, wrappedPacketBytes, startIndex, length);
    }

    @Override
    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }
}
