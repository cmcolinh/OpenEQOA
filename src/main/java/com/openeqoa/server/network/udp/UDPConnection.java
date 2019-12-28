package com.openeqoa.server.network.udp;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.ServerConstants;
import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.*;
import com.openeqoa.server.util.NetworkUtils;

import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
                    processPacket(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, "UDPConnectionListener").start();
    }

    /**
     * Here we process the packet and build a ClientPacket object
     *
     * @param packet The incoming packet from the byte buffer.
     * @param buffer Packet data including extra buffer width.
     */
    private ClientPacket processPacket(DatagramPacket packet) {
        
        byte[] packetBytes = packet.getData();
        
        List<ClientMessage> messages = findMessages(packetBytes);
        
        // User the CRC calculator to get the correct CRC
        // TODO: byte[] crc = CRC.calculateCRC();

        return new ClientPacket.Implementation(packetBytes, messages);

        // Only print out the actual length of the packet and not the buffer...
        //println(getClass(), "Full Length Packet:");
        //for (int i = 0; i < packet.getLength(); i++) {
        //    System.out.print(NetworkUtils.byteToHex(buffer[i]) + " ");
        //    buffer[i] = 0;
        //}
        //println(PRINT_DEBUG);
    }

    private List<ClientMessage> findMessages(byte[] packetBytes) {
    	//TODO lots of lazy hard coding here.  I'm sure that not all packets are following these rules.
    	final int HEADER_BREAK_INDEX = 16;
    	
        int index = HEADER_BREAK_INDEX;
        
        return messageStreamFrom(packetBytes, index)
        	.collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
    
    private Stream<ClientMessage> messageStreamFrom(byte[] packetBytes, int startIndex) {
    	Iterable<ClientMessage> messageIterable = () -> new PacketByteMessageIterator(packetBytes, startIndex);
    	return StreamSupport.stream(messageIterable.spliterator(), false);
    }

    @AllArgsConstructor
    public static class PacketByteMessageIterator implements Iterator<ClientMessage>{
    	public static final int MESSAGE_SEPARATOR = 0xFB;
    	
    	public static final Map<Short, MakeClientMessage> createMessageFromOpcode = Map.ofEntries(
    			Map.entry((short)0x0009, SendReportMessage::new),
    			Map.entry((short)0x000A, PrepareItemOnHotbarRequestMessage::new),
    			Map.entry((short)0x002A, CharacterSelectMessage::new),
    			Map.entry((short)0x002B, CharacterCreateMessage::new),
    			Map.entry((short)0x002C, CharacterViewMessage::new),
    			Map.entry((short)0x002D, CharacterDeleteMessage::new),
    			Map.entry((short)0x00C8, RollRequestMessage::new));
    	
    	private final byte[] packetBytes;
    	
    	private int index;
    	
		@Override
		public boolean hasNext() {
			return packetBytes.length > (index + 5) && packetBytes[index] == MESSAGE_SEPARATOR;
		}

		@Override
		public ClientMessage next() {
			int length = getLength();
			ClientMessage message = createMessageFromOpcode.get(opcode()).with(packetBytes, index + 3, length);
			index = index + length + 3;
			return message;
		}
		
		private int getLength() {
			return packetBytes[index];
		}
		
		private short opcode() {
			short opcode = packetBytes[index + 2];
			opcode = (short) ((opcode << 8) | packetBytes[index + 1]);

			return opcode;
		}
		
		@FunctionalInterface
		private static interface MakeClientMessage
		{
			public ClientMessage with(byte[] packetBytes, int startIndex, int messageLength);
		}
    }
}
