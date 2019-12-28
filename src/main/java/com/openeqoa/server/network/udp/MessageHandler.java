package com.openeqoa.server.network.udp;

import com.openeqoa.server.network.udp.in.packet.message.*;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;

public interface MessageHandler {
	/** take action based on the type of ClientMessage encountered */
	default void handle(ClientMessage message) {
		message.accept(this);
	}

	/** Handle a SendReportMessage (opcode 0x0009) */
	public void visit(SendReportMessage message); //0x0009 - Send Report (Client)

	/** Handle a PrepareItemOnHotbarRequestMessage (opcode 0x000A) */
	public void visit(PrepareItemOnHotbarRequestMessage message); //0x000A - Prepare Item on Hotbar Request (client)

	/** Handle a CharacterSelectMessage (opcode 0x002A) */
	public void visit(CharacterSelectMessage message); //0x002A - Character Select

	/** Handle a CharacterCreateMessage (opcode 0x002B) */
	public void visit(CharacterCreateMessage message); //0x002B - Character Create

	/** Handle a CharacterViewMessage (opcode 0x002C) */
	public void visit(CharacterViewMessage message); //0x002C - Character View

	/** Handle a CharacterDeleteMessage (opcode 0x002D) */
	public void visit(CharacterDeleteMessage message); //0x002D - Character Delete

	/** Handle a RollRequestMessage (opcode 0x00C8) */
	public void visit(RollRequestMessage message); //0x00C8 - Roll Request
}
