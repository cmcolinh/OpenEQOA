package com.openeqoa.server.network.udp.in.packet.message.handler;

import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.CharacterCreateMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterDeleteMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterSelectMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterViewMessage;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;

public interface MessageHandler {
	/** handle all messages in this packet */
	default void handle(ClientPacket packet) {
		packet.getMessages().stream().forEach(this::handle);
	}

	/** take action based on the type of ClientMessage encountered */
	default void handle(ClientMessage message) {
		message.accept(this);
	}

	/** Handle a SendReportMessage (opcode 0x0009) */
	default void visit(SendReportMessage message) {
	} // 0x0009 - Send Report (Client)

	/** Handle a PrepareItemOnHotbarRequestMessage (opcode 0x000A) */
	default void visit(PrepareItemOnHotbarRequestMessage message) {
	} // 0x000A - Prepare Item on Hotbar Request (client)

	/** Handle a CharacterSelectMessage (opcode 0x002A) */
	default void visit(CharacterSelectMessage message) {
	} // 0x002A - Character Select

	/** Handle a CharacterCreateMessage (opcode 0x002B) */
	default void visit(CharacterCreateMessage message) {
	} // 0x002B - Character Create

	/** Handle a CharacterViewMessage (opcode 0x002C) */
	default void visit(CharacterViewMessage message) {
	} // 0x002C - Character View

	/** Handle a CharacterDeleteMessage (opcode 0x002D) */
	default void visit(CharacterDeleteMessage message) {
	} // 0x002D - Character Delete

	/** Handle a RollRequestMessage (opcode 0x00C8) */
	default void visit(RollRequestMessage message) {
	} // 0x00C8 - Roll Request
}
