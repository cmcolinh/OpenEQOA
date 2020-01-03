package com.openeqoa.server.network.udp.out.packet.message;

@FunctionalInterface
public interface ServerMessage {
	/** Get the bytes */
	public byte[] getMessageBytes();

	@FunctionalInterface
	public static interface Builder {
		public ServerMessage build();
	}
}
