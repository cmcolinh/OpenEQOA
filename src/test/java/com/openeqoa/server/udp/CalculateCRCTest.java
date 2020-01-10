package com.openeqoa.server.udp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openeqoa.server.network.udp.CalculateCRC;

public class CalculateCRCTest {
    private CalculateCRC calculateCRC;
    
    public CalculateCRCTest() {
    	calculateCRC = CalculateCRC.getInstance();
    }

    @Test
    @DisplayName("creates expected CRC code for packet for Matt's Packet 156")
    public void testPacket() {
    	byte[] packetToTest = packetOne();
    	byte[] crc = calculateCRC.apply(packetToTest);
    	assertEquals("7c 69 b6 9e", packetBytesAsHexString(crc));
    }

    private byte[] packetOne() {
    	return new byte[] {
    			(byte) 0xB0, (byte) 0x73, (byte) 0x5A, (byte) 0xE7, (byte) 0x95, (byte) 0x60, (byte) 0x5A, (byte) 0xE7, (byte) 0x05, (byte) 0x00, (byte) 0x63, (byte) 0x5A, (byte) 0xE7, (byte) 0x05, (byte) 0x00, (byte) 0x01,
    			(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0xFB, (byte) 0x06, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x25, (byte) 0x00, (byte) 0x00, (byte) 0x00,
    			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 //CRC placeholder bytes, should not be used in the calculation, this is where to put the result
    	};
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
