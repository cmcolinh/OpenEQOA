package com.openeqoa.server.network.udp.in.packet;

import java.util.List;

import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ClientPacket {
	/** Get the Client end point for the message. It is a 2 byte (short) value */
	public short getClientId();

	/** Get the server end point for the message. It is a 2 byte (short) value */
	public short getServerId();

	/** Get the message session ID related to this packet */
	public int getSessionId();

	/** Get the routine that this packet covers */
	public byte getPacketRoutine();

	/** Get the packet number */
	public short getPacketNumber();

	/** Get the messages contained in this packet */
	public List<ClientMessage> getMessages();

	@AllArgsConstructor
	public static class Implementation implements ClientPacket {
		private final byte[] packetBytes;

		@Getter
		private final List<ClientMessage> messages;

		@Override
		public short getClientId() {
			short clientId = packetBytes[1];
			clientId = (short) ((clientId << 8) | packetBytes[0]);

			return clientId;
		}

		@Override
		public short getServerId() {
			short serverId = packetBytes[3];
			serverId = (short) ((serverId << 8) | packetBytes[2]);

			return serverId;
		}

		@Override
		public int getSessionId() {
			int sessionId = packetBytes[9];
			sessionId = (sessionId << 8) | packetBytes[8];
			sessionId = (sessionId << 8) | packetBytes[7];
			sessionId = (sessionId << 8) | packetBytes[6];

			return sessionId;
		}

		@Override
		public byte getPacketRoutine() {
			return packetBytes[5];
		}

		public short getPacketNumber() {
			short packetNumber = packetBytes[15];
			packetNumber = (short) ((packetNumber << 8) | packetBytes[14]);

			return packetNumber;
		}
	}
}
