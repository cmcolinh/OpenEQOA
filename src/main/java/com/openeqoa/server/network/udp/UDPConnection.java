package com.openeqoa.server.network.udp;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.ServerConstants;
import com.openeqoa.server.network.udp.in.packet.message.*;
import com.openeqoa.server.util.NetworkUtils;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

import static com.openeqoa.server.util.Log.println;

public class UDPConnection {

    private final static boolean PRINT_DEBUG = true;

    private final EventBus eventBus = new EventBus();
    private DatagramSocket serverSocket;

    /**
     * Opens a server on a given socket and registers event listeners.
     *
     * @param registerListeners Listeners to listen to.
     */
    public void openServer(Consumer<EventBus> registerListeners) {

        // Creates a socket to allow for communication between clients and the server.
        try {
            serverSocket = new DatagramSocket(ServerConstants.GAME_SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        println(getClass(), "UDP Server opened on port: " + serverSocket.getLocalPort());
        registerListeners.accept(eventBus);
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
                    processPacket(packet, buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "UDPConnectionListener").start();
    }

    /**
     * Here we process the packet and break it down before it reaches the even bus.
     *
     * @param packet The incoming packet from the byte buffer.
     * @param buffer Packet data including extra buffer width.
     */
    private void processPacket(DatagramPacket packet) {
        
        byte[] packetBytes = packet.getData();
        
        int[] messageStartIndices = findMessages(packetBytes);
        
        // Process packet header

        // User the CRC calculator to get the correct CRC
        // TODO: byte[] crc = CRC.calculateCRC();

        // TODO: Send data to the packet event bus for processing...
//        eventBus.publish(opcode, ServerMain.getInstance().getClientManager().getClient(packet.getAddress().getHostAddress()));


        // Only print out the actual length of the packet and not the buffer...
        //println(getClass(), "Full Length Packet:");
        //for (int i = 0; i < packet.getLength(); i++) {
        //    System.out.print(NetworkUtils.byteToHex(buffer[i]) + " ");
        //    buffer[i] = 0;
        //}
        //println(PRINT_DEBUG);
    }

    private List<> findMessages(byte[] packetBytes) {
    	//TODO lots of lazy hard coding here.  I'm sure that not all packets are following these rules.
    	final int HEADER_BREAK_INDEX = 16;
    	
        int index = HEADER_BREAK_INDEX;
    	

    }

    @AllArgsConstructor
    public static class PacketByteMessageIterator implements Iterator<ClientMessage>{
    	public static final int MESSAGE_SEPARATOR = 0xFB;
    	
    	public static final Map<Short, MakeClientMessage> createMessageFromOpcode = Map.ofEntries(
    			Map.entry((short)0x0009, SendReportMessage::new));
    	
    	private final byte[] packetBytes;
    	
    	private int index;
    	
		@Override
		public boolean hasNext() {
			return packetBytes.length > (index + 5) && packetBytes[index] == MESSAGE_SEPARATOR;
		}

		@Override
		public ClientMessage next() {
			int length = getLength();
		}
		
		private int getLength() {
			return packetBytes[index];
		}
		
		@FunctionalInterface
		private static interface MakeClientMessage
		{
			public ClientMessage from(byte[] packetBytes, int startIndex, int messageLength);
		}
    }
    
    Iterator<ClientMessage> messageStream(byte[] packetBytes, int startIndex){
    	final int MESSAGE_SEPARATOR = 0xFB;
    	return new Iterator<ClientMessage>() {
    		int index = startIndex;


    	}
    }
    
    private void printByteArray(String message, byte[] array) {
        for (byte b : array) println(getClass(), message + ": " + NetworkUtils.byteToHex(b));
    }
}
