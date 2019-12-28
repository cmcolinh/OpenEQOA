package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.MessageHandler;

/**
 * Model for client packets with opcode 0x00C8
 * @author colin
 *
 */
public final class RollRequestMessage extends AbstractPacketWrappingClientMessage {
	public RollRequestMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
		super(wrappedPacketBytes, startIndex, length);
	}

	@Override
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}

