package com.openeqoa.server.network.udp.out.packet;

import java.util.Arrays;

import com.openeqoa.server.network.udp.CalculateCRC;

public class AvailableServersPacketBuilder implements ServerPacket.Builder {
    private byte[] packetBytes;

    private final CalculateCRC calculateCRC;

    public AvailableServersPacketBuilder(CalculateCRC calculateCRC) {
        this.calculateCRC = calculateCRC;

        packetBytes = new byte[] { // stubbed bytes TODO: create the packet for real
                (byte) 0xb0, (byte) 0x73, (byte) 0x5a, (byte) 0xe7, (byte) 0x9c, (byte) 0x62, (byte) 0x5a, (byte) 0xe7,
                (byte) 0x05, (byte) 0x00, (byte) 0x20, (byte) 0x02, (byte) 0x00, (byte) 0xfc, (byte) 0xff, (byte) 0x15,
                (byte) 0x01, (byte) 0xb3, (byte) 0x07, (byte) 0x0e, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x43, (byte) 0x00, (byte) 0x61, (byte) 0x00, (byte) 0x73, (byte) 0x00, (byte) 0x74, (byte) 0x00,
                (byte) 0x6c, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x4c, (byte) 0x00,
                (byte) 0x69, (byte) 0x00, (byte) 0x67, (byte) 0x00, (byte) 0x68, (byte) 0x00, (byte) 0x74, (byte) 0x00,
                (byte) 0x77, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x6c, (byte) 0x00, (byte) 0x66, (byte) 0x00,
                (byte) 0x00, (byte) 0x0a, (byte) 0x1f, (byte) 0x57, (byte) 0x27, (byte) 0x30, (byte) 0x0a, (byte) 0x6c,
                (byte) 0xc7, (byte) 0x00, (byte) 0x0a, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x44, (byte) 0x00,
                (byte) 0x69, (byte) 0x00, (byte) 0x72, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x6e, (byte) 0x00,
                (byte) 0x20, (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x6c, (byte) 0x00,
                (byte) 0x64, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x3e, (byte) 0x56, (byte) 0x27, (byte) 0x48,
                (byte) 0x0a, (byte) 0x6c, (byte) 0xc7, (byte) 0x00, (byte) 0x0d, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x46, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x72, (byte) 0x00, (byte) 0x72, (byte) 0x00,
                (byte) 0x61, (byte) 0x00, (byte) 0x6e, (byte) 0x00, (byte) 0x27, (byte) 0x00, (byte) 0x73, (byte) 0x00,
                (byte) 0x20, (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x70, (byte) 0x00,
                (byte) 0x65, (byte) 0x00, (byte) 0x00, (byte) 0x1c, (byte) 0x24, (byte) 0x56, (byte) 0x27, (byte) 0x74,
                (byte) 0x0a, (byte) 0x6c, (byte) 0xc7, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x48, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x73, (byte) 0x00,
                (byte) 0x74, (byte) 0x00, (byte) 0x6f, (byte) 0x00, (byte) 0x63, (byte) 0x00, (byte) 0x6b, (byte) 0x00,
                (byte) 0x00, (byte) 0x91, (byte) 0xb9, (byte) 0x56, (byte) 0x27, (byte) 0x84, (byte) 0x0a, (byte) 0x6c,
                (byte) 0xc7, (byte) 0x00, (byte) 0x0b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x4d, (byte) 0x00,
                (byte) 0x61, (byte) 0x00, (byte) 0x72, (byte) 0x00, (byte) 0x72, (byte) 0x00, (byte) 0x27, (byte) 0x00,
                (byte) 0x73, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x69, (byte) 0x00,
                (byte) 0x73, (byte) 0x00, (byte) 0x74, (byte) 0x00, (byte) 0x00, (byte) 0x9e, (byte) 0xbd, (byte) 0x57,
                (byte) 0x27, (byte) 0x25, (byte) 0xc8, (byte) 0x6c, (byte) 0xc7, (byte) 0x00, (byte) 0x11, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x50, (byte) 0x00, (byte) 0x72, (byte) 0x00, (byte) 0x6f, (byte) 0x00,
                (byte) 0x75, (byte) 0x00, (byte) 0x64, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0x69, (byte) 0x00,
                (byte) 0x6e, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x20, (byte) 0x00, (byte) 0x4f, (byte) 0x00,
                (byte) 0x75, (byte) 0x00, (byte) 0x74, (byte) 0x00, (byte) 0x70, (byte) 0x00, (byte) 0x6f, (byte) 0x00,
                (byte) 0x73, (byte) 0x00, (byte) 0x74, (byte) 0x00, (byte) 0x00, (byte) 0xa9, (byte) 0xd7, (byte) 0x56,
                (byte) 0x27, (byte) 0x44, (byte) 0xc8, (byte) 0x6c, (byte) 0xc7, (byte) 0x00, (byte) 0x0d, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x61, (byte) 0x00, (byte) 0x67, (byte) 0x00,
                (byte) 0x6c, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x79, (byte) 0x00, (byte) 0x20, (byte) 0x00,
                (byte) 0x28, (byte) 0x00, (byte) 0x54, (byte) 0x00, (byte) 0x65, (byte) 0x00, (byte) 0x73, (byte) 0x00,
                (byte) 0x74, (byte) 0x00, (byte) 0x29, (byte) 0x00, (byte) 0x01, (byte) 0x35, (byte) 0x9b, (byte) 0x56,
                (byte) 0x27, (byte) 0xeb, (byte) 0x0a, (byte) 0x6c, (byte) 0xc7, (byte) 0x00, (byte) 0xc6, (byte) 0x2e,
                (byte) 0x9e, (byte) 0x70 };
    }

    /** serverId (2 byte value) */
    public AvailableServersPacketBuilder serverId(short serverId) {
        packetBytes[0] = (byte) (serverId & 0xff);
        packetBytes[1] = (byte) ((serverId >> 8) & 0xff);
        return this;
    }

    /** clientId (2 byte value) */
    public AvailableServersPacketBuilder clientId(short clientId) {
        packetBytes[2] = (byte) (clientId & 0xff);
        packetBytes[3] = (byte) ((clientId >> 8) & 0xff);
        return this;
    }

    /** the sessionid (written in the packet header */
    public AvailableServersPacketBuilder sessionId(int sessionId) {
        packetBytes[6] = (byte) (sessionId & 0xFF);
        packetBytes[7] = (byte) ((sessionId >> 8) & 0xFF);
        packetBytes[8] = (byte) ((sessionId >> 16) & 0xFF);
        packetBytes[9] = (byte) ((sessionId >> 24) & 0xFF);
        return this;
    }

    /** current server bundle number */
    public AvailableServersPacketBuilder currentBundle(int bundle) {
        packetBytes[11] = (byte) (bundle & 0x0000FF);
        packetBytes[12] = (byte) ((bundle << 8) & 0x0000FF);
        return this;
    }

    @Override
    public ServerPacket build() { // TODO: Make a dynamic packet, this is currentlu just
        // a copy of Matt's PCAP 158 since this packet will have a variable length,
        // I might not want to implement the builder with a byte[] and instead just
        // assemble the byte array only here in the build() method.
        byte[] basePacket = Arrays.copyOfRange(packetBytes, 0, 294);
        byte[] crcBytes = calculateCRC.apply(basePacket);
        packetBytes[294] = crcBytes[0];
        packetBytes[295] = crcBytes[1];
        packetBytes[296] = crcBytes[2];
        packetBytes[297] = crcBytes[3];
        return () -> packetBytes.clone();
    }
}
