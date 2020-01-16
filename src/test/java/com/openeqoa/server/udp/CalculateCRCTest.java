package com.openeqoa.server.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.udp.Packets;

public class CalculateCRCTest {
    private CalculateCRC calculateCRC;

    public CalculateCRCTest() {
        calculateCRC = CalculateCRC.getInstance();
    }

    @Test
    @DisplayName("creates expected CRC code for packet for Dudder's Packet 156")
    public void testPacket() throws Exception {
        byte[] packetToTest = Packets.getServerPacket(156);
        byte[] crc = calculateCRC.apply(packetToTest);
        assertEquals("7c 69 b6 9e", Packets.packetBytesAsHexString(crc));
    }

}
