package com.openeqoa.server.network.udp;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.ServerConstants;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static com.openeqoa.server.util.Log.println;

public class UDPConnection {

    private DatagramSocket serverSocket;

    /**
     * Opens a server on a given socket and registers event listeners.
     *
     * @param registerListeners Listeners to listen to.
     */
    public void openServer() {

        // Creates a socket to allow for communication between clients and the server.
        try{
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
        //get a 0 indexed view of the bytes received (and free the buffer to be reused by other packets)
        byte[] packetBytes = getPacketContentsFromBuffer(packet.getData(), packet.getOffset(), packet.getLength());

        new Thread(ProcessUDPPacket.withBytes(packetBytes)).run();
    }

    private byte[] getPacketContentsFromBuffer(byte[] buffer, int offset, int packetLength) {
        byte[] packetBytes = new byte[packetLength];
        for(int index = 0; index < packetLength; index++) {
            packetBytes[index] = buffer[(offset + index) % buffer.length];
        }
        return packetBytes;
    }
}
