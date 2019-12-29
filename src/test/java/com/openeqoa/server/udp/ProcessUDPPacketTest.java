package com.openeqoa.server.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openeqoa.server.network.udp.ProcessUDPPacket;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;

class ProcessUDPPacketTest {
	private ProcessUDPPacket processUDPPacket;

	public ProcessUDPPacketTest() {
		byte[] samplePacketBytes = new byte[] { (byte) 0xde, (byte) 0xc4, // client code
				(byte) 0x91, (byte) 0xb9, // server code
				(byte) 0xc2, // related to length
				(byte) 0x70, // packet router code [70 is player creation routine]
				(byte) 0xf1, (byte) 0x46, (byte) 0x32, (byte) 0x13, // session
				(byte) 0xd8, (byte) 0xbc, (byte) 0x0d, // unknown, but stays with all the packets in this group
				(byte) 0x20, // packet type
				(byte) 0x0e, (byte) 0x00, // Client Packet number
				(byte) 0xfb, // Message Separator
				(byte) 0x06, // Length of Message
				(byte) 0x01, (byte) 0x00, // Message Number in Packet
				(byte) 0x2d, (byte) 0x00, (byte) 0x90, (byte) 0xa0, (byte) 0x8b, (byte) 0x01, //
				(byte) 0xfb, // Message Separator
				(byte) 0x31, // Message Length
				(byte) 0x02, (byte) 0x00, // Message Number in Packet
				(byte) 0x2b, (byte) 0x00, // Message Opcode (2b 00) = character creation packet
				(byte) 0x06, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Username Length
				(byte) 0x41, (byte) 0x61, (byte) 0x61, (byte) 0x61, (byte) 0x61, (byte) 0x61, // Username (Aaaaaa)
				(byte) 0x02, // Level (1) levels are expressed in packets as double the actual value
				(byte) 0x0c, // Race
				(byte) 0x0c, // Class
				(byte) 0x02, // Sex (02 = Female)
				(byte) 0x08, // Hair Color
				(byte) 0x04, // Hair Style
				(byte) 0x04, // Hair Length
				(byte) 0x04, // Face
				(byte) 0x00, // Unknown
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, // training points
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
				(byte) 0x09, (byte) 0x2c, (byte) 0xf9, (byte) 0xa2 }; // CRC
		processUDPPacket = ProcessUDPPacket.withBytes(samplePacketBytes);
	}

	@Test
	@DisplayName("creates two message objects")
	void createsTwoObjects() throws Exception {
		List<ClientMessage> messages = findMessages(processUDPPacket);
		assertEquals(2, messages.size());
	}

	@SuppressWarnings("unchecked")
	private List<ClientMessage> findMessages(ProcessUDPPacket processUDPPacket) throws Exception {
		Field field = processUDPPacket.getClass().getDeclaredField("packetBytes");
		field.setAccessible(true);

		byte[] packetBytes = (byte[]) field.get(processUDPPacket);

		Method m = processUDPPacket.getClass().getDeclaredMethod("findMessages", byte[].class);
		m.setAccessible(true);
		return (List<ClientMessage>) m.invoke(processUDPPacket, packetBytes);
	}
}
