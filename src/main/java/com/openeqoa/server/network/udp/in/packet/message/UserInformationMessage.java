package com.openeqoa.server.network.udp.in.packet.message;

import java.net.InetAddress;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x0904
 * 
 * @author colin
 *
 */
public class UserInformationMessage extends AbstractPacketWrappingClientMessage {
	public UserInformationMessage(InetAddress ipAddress, byte[] wrappedPacketBytes, int startIndex, int length) {
		super(ipAddress, wrappedPacketBytes, startIndex, length);
	}

	/** returns the ip address associated with this request */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	@Override
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
