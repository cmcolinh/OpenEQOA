package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x0009
 * 
 * @author colin
 *
 */
public class SendReportMessage extends AbstractPacketWrappingClientMessage {
    public SendReportMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
        super(wrappedPacketBytes, startIndex, length);
    }

    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }
}
