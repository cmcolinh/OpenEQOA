package com.openeqoa.server.network.udp.in.packet;

import java.net.InetAddress;
import java.util.List;

import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

public interface ClientPacket {
    /** Get the IP Address from which the request came */
    public InetAddress ipAddress();

    /** Get the Client end point for the message. It is a 2 byte (short) value */
    public short clientId();

    /** Get the server end point for the message. It is a 2 byte (short) value */
    public short serverId();

    /** Get the packet routine (first nibble only of the sixth byte) */
    public byte packetRoutine();

    /**
     * Get the packet length (second nibble of the sixth byte plus the full fifth
     * byte)
     */
    public int packetLength();

    /** Get the message session ID related to this packet */
    public int sessionId();

    /** Get the packet number */
    public int packetNumber();

    /** Get the server packet bundle to which this is a response */
    default int getLastBundle() {
        return 1;
    }

    /** Get the messages contained in this packet */
    public List<ClientMessage> messages();

    @AllArgsConstructor
    @Accessors(fluent = true, chain = true)
    public static class Implementation implements ClientPacket {
        @Getter
        private final InetAddress ipAddress;

        private final byte[] packetBytes;

        @Getter
        private final List<ClientMessage> messages;

        @Override
        public short clientId() {
            short clientId = packetBytes[1];
            clientId = (short) ((clientId << 8) | packetBytes[0]);

            return clientId;
        }

        @Override
        public short serverId() {
            short serverId = packetBytes[3];
            serverId = (short) ((serverId << 8) | packetBytes[2]);

            return serverId;
        }

        @Override
        public int sessionId() {
            int sessionId = packetBytes[9];
            sessionId = (sessionId << 8) | packetBytes[8];
            sessionId = (sessionId << 8) | packetBytes[7];
            sessionId = (sessionId << 8) | packetBytes[6];

            return sessionId;
        }

        @Override
        public byte packetRoutine() {
            return (byte) (packetBytes[5] >> 4 & 0x0F);
        }

        @Override
        public int packetLength() {
            int packetLength = packetBytes[5] & 0x0000000F;
            packetLength = ((packetLength << 8) | (packetBytes[4] & 0x000000FF));
            return packetLength;
        }

        public int packetNumber() {
            int packetNumber = (packetBytes[15] & 0x000000FF);
            packetNumber = ((packetNumber << 8) | packetBytes[14]);

            return packetNumber;
        }
    }
}
