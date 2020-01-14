package com.openeqoa.server.network.udp;

import static com.openeqoa.server.util.Log.println;

import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.client.UDPClientHandler;
import com.openeqoa.server.network.client.UDPClientManager;
import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.in.packet.message.handler.SessionInitiatorRoutineMessageHandler;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;
import com.openeqoa.server.network.udp.out.processor.ProcessLoginPacket;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * This class will process the bytes of a given UDP packet, parsing the packet
 * to extract the client messages and the message handler
 * 
 * @author colin
 *
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessUDPEstablishConnectionPacket implements Runnable {
    private static final short serverId = ServerMain.getInstance().getLoginServerId();

    public static final Map<Byte, MessageHandler> createMessageFromRoutingCode = Map.ofEntries(Map.entry((byte) 0x0E,
            new SessionInitiatorRoutineMessageHandler(ServerMain.getInstance().getUdpConnection(),
                    ServerMain.getInstance().getUdpClients(), ServerMain.getInstance().getLoginServerId(),
                    CalculateCRC.getInstance())));

    private final byte[] packetBytes;

    private final InetAddress ipAddress;

    private final CalculateCRC calculateCRC;

    private final UDPClientManager udpClientManager;

    private UDPClientHandler clientHandler;

    private static final UDPConnection udpConnection = ServerMain.getInstance().getUdpConnection();

    public static ProcessUDPEstablishConnectionPacket withBytes(byte[] packetBytes, InetAddress ipAddress,
            CalculateCRC calculateCRC, UDPClientManager clientManager) {
        return new ProcessUDPEstablishConnectionPacket(packetBytes, ipAddress, calculateCRC, clientManager);
    }

    public void run() {
        if (!validPacket())
            return;
        try {
            println(getClass(), "Searching for messages...");
            List<ClientMessage> messages = findMessages(packetBytes);
            println(getClass(), "" + messages.size() + " Messages found");
            MessageHandler handler = getHandler(packetBytes);
            ClientPacket packet = new ClientPacket.Implementation(ipAddress, packetBytes, messages);
            ProcessPacket processPacket = new ProcessLoginPacket(calculateCRC).serverId(serverId)
                    .clientId(packet.clientId());
            Optional.ofNullable(handler).ifPresent(h -> h.handle(packet, processPacket));
            registerConnection(processPacket);
            sendResponseToPacket(processPacket);
        } catch (Exception e) {
            println(getClass(), "error type: " + e.getStackTrace()[0]);
        }
    }

    private void registerConnection(ProcessPacket processPacket) {
        /** create the client handler for this server and clientId */
        clientHandler = new UDPClientHandler.Implementation(ipAddress, processPacket.clientId(), udpConnection);

        short serverId = processPacket.serverId();
        short clientId = processPacket.clientId();

        /** registers the connection with the client manager */
        udpClientManager.addClient(serverId, clientId, clientHandler);
    }

    /**
     * This handler is strict about what packets it will accept. Since it is
     * expecting a session initiator packet, it will demand that the packet's
     * sessionAction byte (index 6) be 0x21, indicating establishing a new session.
     * It will demand that the bundle type (index 11) be 0x20, indicating a message
     * without ACK
     * 
     * @return whether the packet passes the validation rules
     */
    private boolean validPacket() {
        return packetBytes.length > 16 && packetBytes[6] == (byte) 0x21 && packetBytes[11] == (byte) 0x20;
    }

    private MessageHandler getHandler(byte[] packetBytes) {
        return packetBytes.length > 5 ? createMessageFromRoutingCode.get((byte) (packetBytes[5] >> 4 & 0x0F)) : null;
    }

    private List<ClientMessage> findMessages(byte[] packetBytes) {
        // TODO lots of lazy hard coding here. I'm sure that not all packets are
        // following these rules.

        int index = 14; // Valid packets handled by this handler should *always* have the first 0xFB
                        // here

        println(getClass(), String.format("%4s", "0x" + Integer.toHexString(packetBytes[index])).replaceAll(" ", "0"));

        return messageStreamFrom(packetBytes, index).filter(message -> message != null)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    private Stream<ClientMessage> messageStreamFrom(byte[] packetBytes, int startIndex) {
        Iterable<ClientMessage> messageIterable = () -> new PacketByteMessageIterator(packetBytes, startIndex);
        return StreamSupport.stream(messageIterable.spliterator(), false);
    }

    private void sendResponseToPacket(ProcessPacket processPacket) {
        ServerPacket packet = processPacket.getBuilder().build();
        int messageNum = processPacket.packetNum();
        clientHandler.postReliablePacket(packet);
    }
}
