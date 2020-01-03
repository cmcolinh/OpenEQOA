package com.openeqoa.server.network.client;

import java.util.HashMap;
import java.util.Map;

public class TCPClientManager {

    /**
     * Map: IpAddress -> TCPClientHandler
     */
    private final Map<String, TCPClientHandler> clientMap = new HashMap<>();

    public void addClient(String ipAddress, TCPClientHandler clientHandler) {
        clientMap.put(ipAddress, clientHandler);
    }

    public void removeClient(String ipAddress) {
        clientMap.remove(ipAddress);
    }

    public TCPClientHandler getClient(String ipAddress) {
        return clientMap.get(ipAddress);
    }

    public int getClientsOnline() {
        return clientMap.size();
    }
}
