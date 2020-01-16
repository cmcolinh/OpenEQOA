package com.openeqoa.server.network.udp.in.packet.message.reliable;

import com.openeqoa.server.network.udp.in.packet.message.AbstractPacketWrappingClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x0904
 * 
 * @author colin
 *
 */
public class ClientReliableUserInformationMessage extends AbstractPacketWrappingClientMessage {
    public ClientReliableUserInformationMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
        super(wrappedPacketBytes, startIndex, length);
    }

    @Override
    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }

    public String userName() {
        return "Faxon"; // TODO: clearly this is not the real name....
    }
}
