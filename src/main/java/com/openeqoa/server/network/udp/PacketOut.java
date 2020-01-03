package com.openeqoa.server.network.udp;

import java.io.ObjectOutputStream;

import com.openeqoa.server.network.client.TCPClientHandler;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class PacketOut {

	/**
	 * The client handler that holds the object output stream
	 */
	private final TCPClientHandler clientHandler;

	/**
	 * Sends the packet to the client.
	 */
	public void sendPacket() {
		clientHandler.write(this::createPacket);
	}

	/**
	 * Creates the packet.
	 */
	protected abstract void createPacket(ObjectOutputStream write);
}
