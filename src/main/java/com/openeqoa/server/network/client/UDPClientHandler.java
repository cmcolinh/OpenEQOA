package com.openeqoa.server.network.client;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;
import com.openeqoa.server.network.udp.out.packet.message.ServerMessage;

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
    void postReliablePacket(ServerPacket.Builder packet);

    /** notify the handler that a message was acknowledged by the client */
    void acknowledgeReliablePacket(int messageNum);

    public static class Implementation implements UDPClientHandler {
        /** bundle number for the next packet to be sent */
    	private int nextBundleNum;

    	/** message number for the next packet to be sent */
    	private int nextMessageNum;

        private final Map<Byte, Map<Integer, ServerPacket>> unacknowledgedUnreliableMessages = new HashMap<>();
        
        // TODO: resend packets automatically if no acknowledgement is received within X
        // amount of time on the reliable channel
        private final Map<Integer, ServerMessage> unacknowledgedReliableMessages = new HashMap<>();

        @Getter
        @NonNull
        private InetAddress ipAddress;

        @Getter
        private short clientId;

        @NonNull
        private UDPConnection udpConnection;

        public Implementation(InetAddress ipAddress, short clientId, UDPConnection udpConnection) {
        	this.nextBundleNum = 1;
            this.ipAddress = ipAddress;
            this.clientId = clientId;
            this.udpConnection = udpConnection;
        }
        
        private int nextBundleNum() {
        	int bundleNum = nextBundleNum;
        	nextBundleNum++;
        	return bundleNum;
        }

        private int nextMessageNum() {
        	int messageNum = nextMessageNum;
        	nextMessageNum++;
        	return messageNum;
        }

        @Override
        public void postReliablePacket(ServerPacket.Builder builder) {
        	int bundleNum = nextBundleNum();
        	builder.reliableMessageBuilders()
        	    .stream()
        	    .map(messageBuilder -> messageBuilder.messageNum(nextMessageNum()));
            unacknowledgedPackets.put(packetNumss, packet);
            udpConnection.sendUDPPacket(packet, this);
        }

        @Override
        public void acknowledgeReliablePacket(int packetNum) {
            unacknowledgedPackets.get(channel)
                    .entrySet()
                    .stream()
                    .filter(mapEntry -> removeMapEntry(mapEntry, packetNum, channel))
                    .map(mapEntry -> mapEntry.getKey())
                    .forEach(key -> unacknowledgedPackets.get(channel).remove(key));
        }

        private boolean removeMapEntry(Map.Entry<Integer, ServerPacket> mapEntry, int packetNum, byte channel) {
            return channel == RELIABLE_CHANNEL ? (mapEntry.getKey() <= packetNum) : (mapEntry.getKey() < packetNum);
        }
    }
}
