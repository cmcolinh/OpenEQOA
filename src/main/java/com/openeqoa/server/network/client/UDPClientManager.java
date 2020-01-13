package com.openeqoa.server.network.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Map: SessionId (2byte short) -> UDPClientHandler
 * <p>
 * It is possible that if we use multiple server endpoints, that we will need to
 * keep a centralized list of client endpoints using something like Redis cache
 */
public interface UDPClientManager {

    /** Registers a client to the client manager */
    void addClient(short serverId, short clientId, UDPClientHandler clientHandler);

    /** Deregisters a client from the client manager */
    void removeClient(short serverId, short clientId);

    /** Query whether there is a handler for a given clientId */
    boolean isRegistered(short serverId, short clientId);

    /** Acquire the handler associated with a given id */
    UDPClientHandler getClient(short serverId, short clientId);

    /** Get the number of active connections */
    int clientsOnline();

    public static class SingleServerImplementation implements UDPClientManager {
        private final Map<Integer, UDPClientHandler> udpClientRegistry = new HashMap<>();

        @Override
        public void addClient(short serverId, short clientId, UDPClientHandler clientHandler) {
            int key = serverId << 8 | (clientId & 0x0000FFFF);
            synchronized (udpClientRegistry) {
                if (!udpClientRegistry.containsKey(key)) {
                    udpClientRegistry.put(key, clientHandler);
                }
            }
        }

        @Override
        public void removeClient(short serverId, short clientId) {
            synchronized (udpClientRegistry) {
                udpClientRegistry.remove(serverId << 8 | (clientId & 0x0000FFFF));
            }
        }

        @Override
        public boolean isRegistered(short serverId, short clientId) {
            return udpClientRegistry.containsKey(serverId << 8 | (clientId & 0x0000FFFF));
        }

        @Override
        public UDPClientHandler getClient(short serverId, short clientId) {
            synchronized (udpClientRegistry) {
                return udpClientRegistry.get(serverId << 8 | (clientId & 0x0000FFFF));
            }
        }

        @Override
        public int clientsOnline() {
            return udpClientRegistry.size();
        }
    }
}
