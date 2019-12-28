package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.MessageHandler;

/**
 * This interface represent UDP messages from the client.
 * A single client packet will contain one or more messages.
 * @author colin
 *
 */
public interface ClientMessage {
	/** Get the Client end point for the message. It is a 2 byte (short) value */
	public short getClientId();

	/** Get the server end point for the message. It is a 2 byte (short) value */
	public short getServerId();

	/**
	 * Do whatever the message handler does with this kind of ClientMessage
	 * @param MessageHandler handles this message
	 */
    public void accept(MessageHandler handler);
}