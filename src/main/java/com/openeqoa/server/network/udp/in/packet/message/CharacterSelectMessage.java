package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x002A
 * 
 * @author colin
 *
 */
public final class CharacterSelectMessage extends AbstractPacketWrappingClientMessage {
    public CharacterSelectMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
        super(wrappedPacketBytes, startIndex, length);
    }

    @Override
    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }
}
