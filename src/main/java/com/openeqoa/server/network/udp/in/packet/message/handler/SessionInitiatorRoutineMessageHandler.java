package com.openeqoa.server.network.udp.in.packet.message.handler;

import java.net.InetAddress;
import java.util.Optional;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.client.UDPClientHandler;
import com.openeqoa.server.network.client.UDPClientManager;
import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.GameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.UserInformationMessage;
import com.openeqoa.server.network.udp.in.packet.message.acknowledgement.GameVersionPacketAcknowledgementMessage;
import com.openeqoa.server.network.udp.out.packet.AvailableServersPacketBuilder;
import com.openeqoa.server.network.udp.out.packet.GameVersionPacket;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionInitiatorRoutineMessageHandler implements MessageHandler {
    public static final int FRONTIERS = 0x25;
    public static final short UNKNOWN_SERVER_ID = (short) 0xFFFE;
    public final UDPConnection udpConnection;
    public final UDPClientManager udpClientManager;
    public final CalculateCRC calculateCRC;
    public final short serverId;

    @Override
    public void handle(ClientPacket packet) {
        int sessionId = packet.getSessionId();
        int messageNum = packet.getLastBundle();
        // Send the acknowledgement if there is one to the client handler
        Optional.ofNullable(packet)
                .map(p -> udpClientManager.getClient(p.getClientId()))
                .ifPresent(clientHandler -> clientHandler.acknowledgePacket(sessionId, messageNum));
        // Now handle any messages contained in this packet
        MessageHandler.super.handle(packet);
    }

    @Override
    public void visit(GameVersionMessage message) {
        if (message.getGameVersion() != FRONTIERS) {
            throw new IllegalArgumentException(
                    "The client is not identifying as a EQOA:Frontiers disc.  Only EQOA: Frontiers is supported");
        }
    }

    @Override
    public void visit(UserInformationMessage message) {
        short clientId = message.getClientId();
        UDPClientHandler clientHandler = Optional.ofNullable(message)
                .map(UserInformationMessage::getServerId)
                .filter(sid -> sid == UNKNOWN_SERVER_ID)
                .map(__ -> createNewUDPConnection(message.getClientId(), message.getIpAddress()))
                .orElseGet(() -> udpClientManager.getClient(message.getClientId()));
        if (clientHandler == null) {
            return;
        }

        ServerPacket packet = buildGameVersionPacket(serverId, clientId, message);

        clientHandler.postPacket(packet, message.getSessionId(), 1); // TODO: dynamically get a packet number
    }

    private ServerPacket buildGameVersionPacket(short serverId, short clientId, UserInformationMessage message) {
        GameVersionMessageBuilder outMessage = new GameVersionMessageBuilder().messageNum(1);
        return new GameVersionPacket.Builder(calculateCRC).clientId(clientId)
                .serverId(serverId)
                .sessionId(message.getSessionId())
                .message(outMessage)
                .currentBundle(1) // TODO: Get real numbers
                .lastBundle(1) // TODO: calculate real numbers
                .lastMessage(2) // TODO: calculate real numbers
                .build();
    }

    @Override
    public void visit(GameVersionPacketAcknowledgementMessage message) {
        UDPClientHandler clientHandler = Optional.ofNullable(message)
                .map(GameVersionPacketAcknowledgementMessage::getClientId)
                .map(udpClientManager::getClient)
                .orElseThrow(() -> new IllegalArgumentException("No active session"));
        ServerPacket packet = buildAvailableServersPacket(message);
        clientHandler.postPacket(packet, message.getSessionId(), 2); // TODO: dynamically get a packet number
    }

    private ServerPacket buildAvailableServersPacket(GameVersionPacketAcknowledgementMessage message) {
        return new AvailableServersPacketBuilder(calculateCRC).clientId(message.getClientId())
                .serverId(message.getServerId())
                .sessionId(message.getSessionId())
                .currentBundle(2) // TODO: calculate a real number from somewhere (from udpClientManager?)
                .build(); // TODO: create a dynamic packet
    }

    private UDPClientHandler createNewUDPConnection(short clientId, InetAddress ipAddress) {
        if (udpClientManager.isRegistered(clientId) || ServerMain.getInstance().getServerId() == clientId
                || ServerMain.getInstance().getOtherServers().containsValue(clientId)) {
            return null; // ignore the request, and wait for the client to try again with another ID
        }
        UDPClientHandler clientHandler = new UDPClientHandler.Implementation(ipAddress, 5555, udpConnection);
        udpClientManager.addClient(clientId, clientHandler);
        return clientHandler;
    }
}
