package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.MessageHandler;

public class PrepareItemOnHotbarRequestMessage implements ClientMessage {
	public void accept(MessageHandler handler) {
		handler.visit(this);
	}
}
