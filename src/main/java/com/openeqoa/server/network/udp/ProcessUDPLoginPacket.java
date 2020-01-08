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
import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.CharacterCreationRoutineMessageHandler;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.in.packet.message.handler.SessionInitiatorRoutineMessageHandler;
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
public class ProcessUDPLoginPacket implements Runnable {
    private final short serverId = ServerMain.getInstance().getLoginServerId();

    public static Map<Byte, MessageHandler> createMessageFromRoutingCode = Map.ofEntries(
            Map.entry((byte) 0x07, new CharacterCreationRoutineMessageHandler()),
            Map.entry((byte) 0x0E,
                    new SessionInitiatorRoutineMessageHandler(ServerMain.getInstance().getUdpConnection(),
                            ServerMain.getInstance().getUdpClients(), CalculateCRC.getInstance(),
                            ServerMain.getInstance().getLoginServerId())));

    private final byte[] packetBytes;

    private final InetAddress ipAddress;

    public static ProcessUDPLoginPacket withBytes(byte[] packetBytes, InetAddress ipAddress) {
        return new ProcessUDPLoginPacket(packetBytes, ipAddress);
    }

    public void run() {
        println(getClass(), "Searching for messages...");
        List<ClientMessage> messages = findMessages(packetBytes, ipAddress);
        println(getClass(), "" + messages.size() + " Messages found");
        MessageHandler handler = getHandler(packetBytes);
        ProcessPacket processPacket = new ProcessLoginPacket();
        ClientPacket packet = new ClientPacket.Implementation(ipAddress, packetBytes, messages);
        Optional.ofNullable(handler).ifPresent(h -> h.handle(packet, processPacket));
        sendResponseToPacket(processPacket);
    }

    private MessageHandler getHandler(byte[] packetBytes) {
        return packetBytes.length > 5 ? createMessageFromRoutingCode.get((byte) (packetBytes[5] >> 4 & 0x0F)) : null;
    }

    private List<ClientMessage> findMessages(byte[] packetBytes, InetAddress ipAddress) {
        // TODO lots of lazy hard coding here. I'm sure that not all packets are
        // following these rules.

        int index = findFirstHeaderBreakIndex();

        println(getClass(), "0x" + Integer.toHexString(packetBytes[index]).substring(6, 8));

        return messageStreamFrom(packetBytes, ipAddress, index).filter(message -> message != null)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

    private int findFirstHeaderBreakIndex() {
        return 14;
    }

    private Stream<ClientMessage> messageStreamFrom(byte[] packetBytes, InetAddress ipAddress, int startIndex) {
        Iterable<ClientMessage> messageIterable = () -> new PacketByteMessageIterator(packetBytes, ipAddress,
                startIndex);
        return StreamSupport.stream(messageIterable.spliterator(), false);
    }

    private void sendResponseToPacket(ProcessPacket processPacket) {
    	
    }
}
