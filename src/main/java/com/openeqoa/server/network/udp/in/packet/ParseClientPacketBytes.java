package com.openeqoa.server.network.udp.in.packet;

import java.util.function.Function;

public interface ParseClientPacketBytes extends Function<byte[], ClientPacket> {
	public static class Implementation implements ParseClientPacketBytes {
		public ClientPacket apply(byte[] packetBytes) {
			return null;
		}
	}
}
