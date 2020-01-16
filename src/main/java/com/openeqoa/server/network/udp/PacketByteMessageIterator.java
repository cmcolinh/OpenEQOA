package com.openeqoa.server.network.udp;

import static com.openeqoa.server.util.Log.println;

import java.util.Iterator;
import java.util.Map;

import com.openeqoa.server.network.udp.in.packet.message.CharacterCreateMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterDeleteMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterSelectMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterViewMessage;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;
import com.openeqoa.server.network.udp.in.packet.message.reliable.ClientReliableGameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.reliable.ClientReliableUserInformationMessage;

import lombok.AllArgsConstructor;

/**
 * This class will iterate through the bytes of the packet, reading for message
 * length markers, opcodes, and separator tokens, to parse individual messages
 * from the serialized bytes
 * 
 * @author colins
 *
 */
@AllArgsConstructor
public class PacketByteMessageIterator implements Iterator<ClientMessage> {
    public static final byte MESSAGE_SEPARATOR = (byte) 0xFB;

    public static final Map<Short, MakeClientMessage> createMessageFromOpcode = Map.ofEntries(
            Map.entry((short) 0x0000, ClientReliableGameVersionMessage::new), Map.entry((short) 0x0009, SendReportMessage::new),
            Map.entry((short) 0x000A, PrepareItemOnHotbarRequestMessage::new),
            Map.entry((short) 0x002A, CharacterSelectMessage::new),
            Map.entry((short) 0x002B, CharacterCreateMessage::new),
            Map.entry((short) 0x002C, CharacterViewMessage::new),
            Map.entry((short) 0x002D, CharacterDeleteMessage::new), Map.entry((short) 0x00C8, RollRequestMessage::new),
            Map.entry((short) 0x0904, ClientReliableUserInformationMessage::new));

    private final byte[] packetBytes;

    private int index;

    @Override
    public boolean hasNext() {
        return packetBytes.length > (index + 5) && packetBytes[index] == MESSAGE_SEPARATOR;
    }

    @Override
    public ClientMessage next() {
        int length = getLength();
        ClientMessage message = createMessageFromOpcode.getOrDefault(opcode(), (p, s, l) -> null)
                .with(packetBytes, index + 4, length);
        println(getClass(), "" + message);
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
