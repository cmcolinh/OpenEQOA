package com.openeqoa.server.network.udp.in.packet.message.handler;

import java.net.InetAddress;
import java.util.Optional;

import com.openeqoa.server.ServerMain;
import com.openeqoa.server.network.client.UDPClientHandler;
import com.openeqoa.server.network.client.UDPClientManager;
import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.in.packet.message.GameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.UserInformationMessage;
import com.openeqoa.server.network.udp.out.packet.GameVersionPacketBuilder;
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
        GameVersionMessageBuilder outMessage = new GameVersionMessageBuilder().messageNum(1);
        ServerPacket packet = new GameVersionPacketBuilder(calculateCRC).clientId(clientId)
                .serverId(serverId)
                .sessionId(message.getSessionId())
                .message(outMessage)
                .currentBundle(1) // TODO: Get real numbers
                .lastBundle(1) // TODO: calculate real numbers
                .lastMessage(2) // TODO: calculate real numbers
                .build();
        clientHandler.postPacket(packet, message.getSessionId(), 1);
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
