package com.openeqoa.server.network.udp;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.CharacterCreateMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterDeleteMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterSelectMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterViewMessage;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.CharacterCreationRoutineMessageHandler;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * This class will process the bytes of a given UDP packet, parsing the packet
 * to extract the client messages and the message handler
 * 
 * @author colin
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessUDPPacket implements Runnable {
	public static Map<Byte, MessageHandler> createMessageFromRoutingCode = Map
			.ofEntries(Map.entry((byte) 0x70, new CharacterCreationRoutineMessageHandler()));

	private final byte[] packetBytes;

	public static ProcessUDPPacket withBytes(byte[] packetBytes) {
		return new ProcessUDPPacket(packetBytes);
	}

	public void run() {
		List<ClientMessage> messages = findMessages(packetBytes);
		MessageHandler handler = getHandler(packetBytes);
		ClientPacket packet = new ClientPacket.Implementation(packetBytes, messages);
		Optional.ofNullable(handler).ifPresent(h -> h.handle(packet));
	}

	private MessageHandler getHandler(byte[] packetBytes) {
		return packetBytes.length > 5 ? createMessageFromRoutingCode.get(packetBytes[5]) : null;
	}

	private List<ClientMessage> findMessages(byte[] packetBytes) {
		// TODO lots of lazy hard coding here. I'm sure that not all packets are
		// following these rules.
		final int HEADER_BREAK_INDEX = 16;

		int index = HEADER_BREAK_INDEX;
		return messageStreamFrom(packetBytes, index).filter(message -> message != null)
				.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
	}

	private Stream<ClientMessage> messageStreamFrom(byte[] packetBytes, int startIndex) {
		Iterable<ClientMessage> messageIterable = () -> new PacketByteMessageIterator(packetBytes, startIndex);
		return StreamSupport.stream(messageIterable.spliterator(), false);
	}

	/**
	 * This class will iterate through the bytes of the packet, reading for message
	 * length markers, opcodes, and separator tokens, to parse individual messages
	 * from the serialized bytes
	 * 
	 * @author colin
	 *
	 */
	@AllArgsConstructor
	private static class PacketByteMessageIterator implements Iterator<ClientMessage> {
		public static final byte MESSAGE_SEPARATOR = (byte) 0xFB;

		public static final Map<Short, MakeClientMessage> createMessageFromOpcode = Map.ofEntries(
				Map.entry((short) 0x0009, SendReportMessage::new),
				Map.entry((short) 0x000A, PrepareItemOnHotbarRequestMessage::new),
				Map.entry((short) 0x002A, CharacterSelectMessage::new),
				Map.entry((short) 0x002B, CharacterCreateMessage::new),
				Map.entry((short) 0x002C, CharacterViewMessage::new),
				Map.entry((short) 0x002D, CharacterDeleteMessage::new),
				Map.entry((short) 0x00C8, RollRequestMessage::new));

		private final byte[] packetBytes;

		private int index;

		@Override
		public boolean hasNext() {
			return packetBytes.length > (index + 5) && packetBytes[index] == MESSAGE_SEPARATOR;
		}

		@Override
		public ClientMessage next() {
			int length = getLength();
			ClientMessage message = createMessageFromOpcode.getOrDefault(opcode(), (p, s, l) -> null).with(packetBytes,
					index + 4, length);
			index = index + length + 4;
			return message;
		}

		private int getLength() {
			return packetBytes[index + 1];
		}

		private short opcode() {
			short opcode = packetBytes[index + 5];
			opcode = (short) ((opcode << 8) | packetBytes[index + 4]);

			return opcode;
		}

		@FunctionalInterface
		private static interface MakeClientMessage {
			public ClientMessage with(byte[] packetBytes, int startIndex, int messageLength);
		}
	}
}
