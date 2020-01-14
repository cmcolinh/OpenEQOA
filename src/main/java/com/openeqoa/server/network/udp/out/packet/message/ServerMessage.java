package com.openeqoa.server.network.udp.out.packet.message;

@FunctionalInterface
public interface ServerMessage {
	/** Get the bytes */
	public byte[] getMessageBytes();

	public static interface Builder {
		/** set the message number of the message */
		Builder messageNum(int messageNum);

		/** Build the message */
		ServerMessage build();
	}
}
