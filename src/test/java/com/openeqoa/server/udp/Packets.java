package com.openeqoa.server.udp;

import static com.openeqoa.server.util.NetworkUtils.hexStringToByteArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Packets {
    private static Map<Short, byte[]> clientPacket = null;

    private static Map<Short, byte[]> serverPacket = null;

    public static byte[] getClientPacket(int packetNum) {
        if (clientPacket == null) {
            ClassLoader cl = Packets.class.getClassLoader();
            File file = new File(cl.getResource("./src/test/resources/packet_digests/client.csv").getFile());
            try (BufferedReader r = new BufferedReader(new FileReader(file))) {
                clientPacket = r.lines()
                        .map(l -> Map.entry((short) Integer.parseInt(l.split(",")[0]),
                                hexStringToByteArray(l.split(",")[1])))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            } catch (Exception e) {
            }
        }
        return Optional.ofNullable(clientPacket).map(c -> c.get((short) packetNum)).orElse(null);
    }

    public static byte[] getServerPacket(int packetNum) throws Exception {
        if (serverPacket == null) {
            try (BufferedReader r = new BufferedReader(
                    new FileReader("./src/test/resources/packet_digests/server.csv"))) {
                serverPacket = r.lines()
                        .map(l -> Map.entry((short) Integer.parseInt(l.split(",")[0]),
                                hexStringToByteArray(l.split(",")[1])))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            } catch (Exception e) {
            }
        }
        return Optional.ofNullable(serverPacket).map(c -> c.get((short) packetNum)).map(byte[]::clone).orElse(null);
    }


    public static String packetBytesAsHexString(byte[] array) {
        return IntStream.range(0, array.length)
                .map(idx -> array[idx])
                .mapToObj(b -> Integer.toHexString(b))
                .map(s -> String.format("%8s", s).replace(" ", "0"))
                .map(s -> s.substring(6, 8))
                .collect(Collectors.joining(" "));
    }
}
