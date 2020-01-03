package com.openeqoa.server.network.udp.out.packet;

import java.util.Arrays;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameVersionPacketBuilder implements ServerPacket.Builder {
    private final CalculateCRC calculateCRC;

    protected byte[] packetBytes = new byte[35];

    /** serverId (2 byte value) */
    public GameVersionPacketBuilder serverId(short serverId) {
        packetBytes[0] = (byte) (serverId & 0xff);
        packetBytes[1] = (byte) ((serverId >> 8) & 0xff);
        return this;
    }

    /** clientId (2 byte value) */
    public GameVersionPacketBuilder clientId(short clientId) {
        packetBytes[2] = (byte) (clientId & 0xff);
        packetBytes[3] = (byte) ((clientId >> 8) & 0xff);
        return this;
    }

    /** the sessionid (written in the packet header, and then the bundle header) */
    public GameVersionPacketBuilder sessionId(int sessionId) {
        packetBytes[6] = (byte) (sessionId & 0xFF);
        packetBytes[7] = (byte) ((sessionId >> 8) & 0xFF);
        packetBytes[8] = (byte) ((sessionId >> 16) & 0xFF);
        packetBytes[9] = (byte) ((sessionId >> 24) & 0xFF);
        packetBytes[11] = (byte) (sessionId & 0xFF);
        packetBytes[12] = (byte) ((sessionId >> 8) & 0xFF);
        packetBytes[13] = (byte) ((sessionId >> 16) & 0xFF);
        packetBytes[14] = (byte) ((sessionId >> 24) & 0xFF);
        return this;
    }

    /** current server bundle number */
    public GameVersionPacketBuilder currentBundle(int bundle) {
        packetBytes[15] = (byte) (bundle & 0x0000FF);
        packetBytes[16] = (byte) ((bundle << 8) & 0x0000FF);
        return this;
    }

    /** client bundle for which this packet is a response */
    public GameVersionPacketBuilder lastBundle(int bundle) {
        packetBytes[17] = (byte) (bundle & 0x0000FF);
        packetBytes[18] = (byte) ((bundle << 8) & 0x0000FF);
        return this;
    }

    /**
     * the last message of the previous packet (so the client knows which message
     * you are responding to)
     */
    public GameVersionPacketBuilder lastMessage(int message) {
        packetBytes[19] = (byte) (message & 0x0000FF);
        packetBytes[20] = (byte) ((message << 8) & 0x0000FF);
        return this;
    }

    /** the version message to send (it is a fixed 10 bytes) */
    public GameVersionPacketBuilder message(GameVersionMessageBuilder message) {
        byte[] messageBytes = message.build().getMessageBytes();
        for (int i = 0; i < messageBytes.length; i++) { // messageBytes starts with the FB message divider byte
            packetBytes[i + 21] = messageBytes[i]; // messageBytes should be length 10
        }
        return this;
    }

    @Override
    public ServerPacket build() {
        packetBytes[4] = (byte) 0x95; // packet length
        packetBytes[5] = (byte) 0x60; // packet class 0x06 (second nibble is part of packet length)
        packetBytes[10] = (byte) 0x63; // bundle type
        byte[] basePacket = Arrays.copyOfRange(packetBytes, 0, 31);
        byte[] crcBytes = calculateCRC.apply(basePacket);
        packetBytes[31] = crcBytes[0];
        packetBytes[32] = crcBytes[1];
        packetBytes[33] = crcBytes[2];
        packetBytes[34] = crcBytes[3];
        return () -> packetBytes.clone();
    }
}
