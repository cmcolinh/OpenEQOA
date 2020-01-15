package com.openeqoa.server.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.udp.in.Packets;

public class CalculateCRCTest {
    private CalculateCRC calculateCRC;

    public CalculateCRCTest() {
        calculateCRC = CalculateCRC.getInstance();
    }

    @Test
    @DisplayName("creates expected CRC code for packet for Matt's Packet 156")
    public void testPacket() throws Exception {
        byte[] packetToTest = Packets.getServerPacket(156);
        byte[] crc = calculateCRC.apply(packetToTest);
        assertEquals("7c 69 b6 9e", packetBytesAsHexString(crc));
    }

    private String packetBytesAsHexString(byte[] array) {
        return IntStream.range(0, array.length)
                .map(idx -> array[idx])
                .mapToObj(b -> Integer.toHexString(b))
                .map(s -> String.format("%8s", s).replace(" ", "0"))
                .map(s -> s.substring(6, 8))
                .collect(Collectors.joining(" "));
    }
}
