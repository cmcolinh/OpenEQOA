package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x0009
 * @author colin
 *
 */
public class SendReportMessage extends AbstractPacketWrappingClientMessage {
	public SendReportMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
		super(wrappedPacketBytes, startIndex, length);
		// TODO Auto-generated constructor stub
	}

	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}

