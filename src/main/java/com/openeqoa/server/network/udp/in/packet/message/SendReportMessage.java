package com.openeqoa.server.network.udp.in.packet.message;

import java.net.InetAddress;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x0009
 * 
 * @author colin
 *
 */
public class SendReportMessage extends AbstractPacketWrappingClientMessage {
	public SendReportMessage(InetAddress ipAddress, byte[] wrappedPacketBytes, int startIndex, int length) {
		super(ipAddress, wrappedPacketBytes, startIndex, length);
	}

	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
