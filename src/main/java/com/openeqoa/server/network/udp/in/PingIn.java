package com.openeqoa.server.network.udp.in;

import com.openeqoa.server.network.client.ClientHandler;
import com.openeqoa.server.network.udp.PacketListener;
import com.openeqoa.server.network.udp.out.PingOut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingIn implements PacketListener {

	public void onIncomingPing(ClientHandler clientHandler) {
		log.debug("{}{}", getClass(), "Pong!");
		new PingOut(clientHandler).sendPacket();
	}
}
