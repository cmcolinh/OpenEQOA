package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x000A
 * @author colin
 *
 */
public final class PrepareItemOnHotbarRequestMessage extends AbstractPacketWrappingClientMessage {
	public PrepareItemOnHotbarRequestMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
		super(wrappedPacketBytes, startIndex, length);
	}

	@Override
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
