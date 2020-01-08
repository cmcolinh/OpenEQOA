package com.openeqoa.server.network.udp.out.processor;

import java.util.List;

import com.openeqoa.server.network.udp.out.packet.ServerPacket;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProcessLoginPacket implements ProcessPacket {
    public static enum PacketType {
        UPDATE((byte) 0x20), CONFIRM((byte) 0x23), UPDATE_WITH_CONFIRM((byte) 0x63);

        private final byte value;

        private PacketType(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }

    private PacketType packetType;

    private List<Short> messagesToConfirm; // A list of messages that the client requires confirmation for

    private List<Short> confirmedMessages; // A list of messages to mark confirmed

    private ServerPacket.Builder builder; // the packet that will be scheduled to be sent, or null if no response
}
