package com.openeqoa.server.network.udp;

import static com.openeqoa.server.util.Log.println;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.ServerConstants;
import com.openeqoa.server.network.client.UDPClientHandler;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;

public class UDPConnection {

    private DatagramSocket serverSocket;

    public static final Map<Short, BiFunction<byte[], InetAddress, Runnable>> getEndpoint = Map.ofEntries(
            Map.entry((short) 0x6110, (b, n) -> ProcessUDPLoginPacket.withBytes(b, n)),
            Map.entry((short) 0xFEFF, (b, n) -> ProcessUDPEstablishConnectionPacket.withBytes(b, n)));

    public static final BiFunction<byte[], InetAddress, Runnable> EMPTY_RUNNABLE = (b, n) -> () -> {
    };

    /**
     * Opens a server on a given socket.
     */
    public void openServer() {

        // Creates a socket to allow for communication between clients and the server.
        try {
            serverSocket = new DatagramSocket(ServerConstants.GAME_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        println(getClass(), "UDP Server opened on port: " + serverSocket.getLocalPort());
        listenForPackets();
    }

    /**
     * Listen for incoming client packets.
     */
    private void listenForPackets() {
        println(getClass(), "Listening for incoming packets...");

        byte[] buffer = new byte[576];

        new Thread(() -> {
            while (ServerMain.getInstance().isRunning()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    serverSocket.receive(packet);
                    System.out.println("packet found");
                    processPacket(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "UDPConnectionListener").start();
    }

    /**
     * Here we process the packet
     *
     * @param packet The incoming packet from the byte buffer.
     */
    private void processPacket(DatagramPacket packet) {
        // get a 0 indexed view of the bytes received (and free the buffer to be reused
        // by other packets)
        byte[] packetBytes = getPacketContentsFromBuffer(packet.getData(), packet.getOffset(), packet.getLength());
        println(getClass(), "Packet received: " + packetBytes.length + " bytes");
        println(getClass(), packetBytesAsHexString(packetBytes));
        // validatePacketCRC(); TODO: validateCRC before processing packet

        short serverId = packetBytes.length < 4 ? 0 : ((short) ((packetBytes[3] << 8) | packetBytes[2]));
        new Thread(getEndpoint.getOrDefault(serverId, EMPTY_RUNNABLE).apply(packetBytes, packet.getAddress())).run();
    }

    private byte[] getPacketContentsFromBuffer(byte[] buffer, int offset, int packetLength) {
        byte[] packetBytes = new byte[packetLength];
        for (int index = 0; index < packetLength; index++) {
            packetBytes[index] = buffer[(offset + index) % buffer.length];
        }
        return packetBytes;
    }

    public void sendUDPPacket(ServerPacket packet, UDPClientHandler client) {
        byte[] packetBytes = packet.getPacketBytes();
        println(getClass(), "Sending packet: " + packet.getPacketBytes().length + " bytes");
        println(getClass(), packetBytesAsHexString(packet.getPacketBytes()));

        DatagramPacket outPacket = new DatagramPacket(packetBytes, packetBytes.length, client.getIpAddress(),
                client.getPort());
        try {
            serverSocket.send(outPacket);
        } catch (IOException e) {
            System.out.println("that was bad"); // TODO: something useful
        }
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
