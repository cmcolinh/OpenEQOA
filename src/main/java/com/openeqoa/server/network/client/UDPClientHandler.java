package com.openeqoa.server.network.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;

import lombok.Getter;
import lombok.NonNull;

public interface UDPClientHandler {
    /** The IP address of the client */
    InetAddress getIpAddress();

    /* the client port number */
    int getPort();

    /**
     * send a packet to the client, the Client Handler should handle
     * acknowledgement, and retries
     */
    void postPacket(ServerPacket packet, int sessionId, int messageNum);

    /**
     * notify the handler that a packet was acknowledged by the client
     */
    void acknowledgePacket(int sessionId, int messageNum, MessageHandler messageHandler);

    public static class Implementation implements UDPClientHandler {
        // TODO: resend packets automatically if no acknowledgement is received within X
        // amount of time
        private final Map<Integer, Map<Integer, ServerPacket>> unacknowledgedPackets = new HashMap<>();

        @Getter
        @NonNull
        private InetAddress ipAddress;

        @Getter
        private int port;

        @NonNull
        private UDPConnection udpConnection;

        public Implementation(InetAddress ipAddress, int port, UDPConnection udpConnection) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.udpConnection = udpConnection;
        }

        @Override
        public void postPacket(ServerPacket packet, int sessionId, int messageNum) {
            Map<Integer, ServerPacket> sessionMap = unacknowledgedPackets.getOrDefault(sessionId, new HashMap<>());
            sessionMap.put(messageNum, packet);
            unacknowledgedPackets.put(sessionId, sessionMap);
            udpConnection.sendUDPPacket(packet, this);
        }

        @Override
        public void acknowledgePacket(int sessionId, int messageNum, MessageHandler messageHandler) {
            Optional.ofNullable(unacknowledgedPackets.get(sessionId))
                    .map(sessionMap -> sessionMap.remove(sessionId))
                    .ifPresent(serverPacket -> serverPacket.whenAcknowledged(messageHandler));
        }
    }
}
