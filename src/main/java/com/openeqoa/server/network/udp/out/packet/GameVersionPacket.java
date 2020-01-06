package com.openeqoa.server.network.udp.out.packet;

import static com.openeqoa.server.util.Log.println;

import java.util.Arrays;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.in.packet.message.acknowledgement.GameVersionPacketAcknowledgementMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class GameVersionPacket implements ServerPacket {
    private byte[] bytes;

    public byte[] getPacketBytes() {
        return bytes.clone();
    }

    private short getServerId() {
        short serverId = bytes[1];
        serverId = (short) ((serverId << 8) | bytes[0]);

        return serverId;
    }

    private short getClientId() {
        short clientId = bytes[3];
        clientId = (short) ((clientId << 8) | bytes[2]);

        return clientId;
    }

    private int getSessionId() {
        int sessionId = bytes[9];
        sessionId = (sessionId << 8) | bytes[8];
        sessionId = (sessionId << 8) | bytes[7];
        sessionId = (sessionId << 8) | bytes[6];

        return sessionId;
    }

    public void whenAcknowledged(MessageHandler messageHandler) {
        short serverId = getServerId();
        short clientId = getClientId();
        int sessionId = getSessionId();
        GameVersionPacketAcknowledgementMessage message = new GameVersionPacketAcknowledgementMessage(clientId,
                serverId, sessionId);
        messageHandler.visit(message);
    }

    @RequiredArgsConstructor
    public static class Builder implements ServerPacket.Builder {
        private final CalculateCRC calculateCRC;

        protected byte[] packetBytes = new byte[35];

        /** serverId (2 byte value) */
        public GameVersionPacket.Builder serverId(short serverId) {
            packetBytes[0] = (byte) (serverId & 0xff);
            packetBytes[1] = (byte) ((serverId >> 8) & 0xff);
            return this;
        }

        /** clientId (2 byte value) */
        public GameVersionPacket.Builder clientId(short clientId) {
            packetBytes[2] = (byte) (clientId & 0xff);
            packetBytes[3] = (byte) ((clientId >> 8) & 0xff);
            return this;
        }

        /** the sessionid (written in the packet header, and then the bundle header) */
        public GameVersionPacket.Builder sessionId(int sessionId) {
            println(getClass(), "" + sessionId);
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
        public GameVersionPacket.Builder currentBundle(int bundle) {
            packetBytes[15] = (byte) (bundle & 0x0000FF);
            packetBytes[16] = (byte) ((bundle << 8) & 0x0000FF);
            return this;
        }

        /** client bundle for which this packet is a response */
        public GameVersionPacket.Builder lastBundle(int bundle) {
            packetBytes[17] = (byte) (bundle & 0x0000FF);
            packetBytes[18] = (byte) ((bundle << 8) & 0x0000FF);
            return this;
        }

        /**
         * the last message of the previous packet (so the client knows which message
         * you are responding to)
         */
        public GameVersionPacket.Builder lastMessage(int message) {
            packetBytes[19] = (byte) (message & 0x0000FF);
            packetBytes[20] = (byte) ((message << 8) & 0x0000FF);
            return this;
        }

        /** the version message to send (it is a fixed 10 bytes) */
        public GameVersionPacket.Builder message(GameVersionMessageBuilder message) {
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
            return new GameVersionPacket(packetBytes);
        }
    }
}
