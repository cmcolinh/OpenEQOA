package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x0904
 * 
 * @author colin
 *
 */
public class UserInformationMessage extends AbstractPacketWrappingClientMessage {
    public UserInformationMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
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
