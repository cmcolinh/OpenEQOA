package com.openeqoa.server.network.udp.out;

import java.io.ObjectOutputStream;

import com.openeqoa.server.network.client.ClientHandler;
import com.openeqoa.server.network.udp.PacketOut;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PingOut extends PacketOut {

	public PingOut(ClientHandler clientHandler) {
		super(clientHandler);
	}

	@Override
	protected void createPacket(ObjectOutputStream write) {
		log.debug("{}{}", getClass(), "Ping!");
	}
}
