package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.MessageHandler;

/**
 * Model for client packets with opcode 0x002D
 * @author colin
 *
 */
public final class CharacterDeleteMessage extends AbstractPacketWrappingClientMessage {
	public CharacterDeleteMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
		super(wrappedPacketBytes, startIndex, length);
	}

	@Override
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
