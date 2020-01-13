package com.openeqoa.server.network.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;

import lombok.Getter;
import lombok.NonNull;

public interface UDPClientHandler {
    /** The IP address of the client */
    InetAddress getIpAddress();

    /* the client's sessionId */
    short getClientId();

    /**
     * send a packet to the client, the Client Handler should handle
     * acknowledgement, and retries
     */
    void postPacket(ServerPacket packet, int packetNum);

    /**
     * notify the handler that a packet was acknowledged by the client
     */
    void acknowledgePacket(int messageNum);

    public static class Implementation implements UDPClientHandler {
        // TODO: resend packets automatically if no acknowledgement is received within X
        // amount of time
        private final Map<Integer, ServerPacket> unacknowledgedPackets = new HashMap<>();

        @Getter
        @NonNull
        private InetAddress ipAddress;

        @Getter
        private short clientId;

        @NonNull
        private UDPConnection udpConnection;

        public Implementation(InetAddress ipAddress, short clientId, UDPConnection udpConnection) {
            this.ipAddress = ipAddress;
            this.clientId = clientId;
            this.udpConnection = udpConnection;
        }

        @Override
        public void postPacket(ServerPacket packet, int packetNum) {
            unacknowledgedPackets.put(packetNum, packet);
            udpConnection.sendUDPPacket(packet, this);
        }

        @Override
        public void acknowledgePacket(int packetNum) {
            unacknowledgedPackets.entrySet()
                    .stream()
                    .filter(mapEntry -> mapEntry.getKey() <= packetNum)
                    .map(mapEntry -> mapEntry.getKey())
                    .forEach(key -> unacknowledgedPackets.remove(key));
        }
    }
}
