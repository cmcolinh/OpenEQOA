package com.openeqoa.server.udp;

import com.openeqoa.server.network.client.UDPClientHandler;
import com.openeqoa.server.network.client.UDPClientManager;

public class MockUDPClientManager implements UDPClientManager {

    @Override
    public void addClient(short serverId, short clientId, UDPClientHandler clientHandler) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeClient(short serverId, short clientId) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isRegistered(short serverId, short clientId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public UDPClientHandler getClient(short serverId, short clientId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int clientsOnline() {
        // TODO Auto-generated method stub
        return 0;
    }

}
