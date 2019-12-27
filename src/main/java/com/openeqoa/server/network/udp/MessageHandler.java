package com.openeqoa.server.network.udp;

import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;

public interface MessageHandler {
	default void handle(ClientMessage message) {
		message.accept(this);
	}

	public void visit(SendReportMessage message); //0x0009 - Send Report (Client)

	public void visit(PrepareItemOnHotbarRequestMessage message); //0x000A - Prepare Item on Hotbar Request (client)

	public void visit(RollRequestMessage message); //0x00C8 - Roll Request
}
