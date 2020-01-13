package com.openeqoa.server.network.udp.out.packet;

import static com.openeqoa.server.util.Log.println;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class GameVersionPacket implements ServerPacket {
    private byte[] bytes;

    public byte[] getPacketBytes() {
        return bytes.clone();
    }

    // public void whenAcknowledged(MessageHandler messageHandler, ProcessPacket
    // processPacket) {
    // short serverId = getServerId();
    // short clientId = getClientId();
    // int sessionId = getSessionId();
    // messageHandler.visit(message, processPacket);
    // }

    @RequiredArgsConstructor
    public static class Builder implements ServerPacket.Builder {
        private final CalculateCRC calculateCRC;

        protected byte[] packetBytes = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x95,
                (byte) 0x60, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x63, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };

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
            byte[] crcBytes = calculateCRC.apply(packetBytes);
            packetBytes[31] = crcBytes[0];
            packetBytes[32] = crcBytes[1];
            packetBytes[33] = crcBytes[2];
            packetBytes[34] = crcBytes[3];
            return new GameVersionPacket(packetBytes);
        }
    }
}
