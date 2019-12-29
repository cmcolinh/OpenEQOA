package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x002C
 * @author colin
 *
 */
public final class CharacterViewMessage extends AbstractPacketWrappingClientMessage {
	public CharacterViewMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
		super(wrappedPacketBytes, startIndex, length);
	}

	@Override
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
