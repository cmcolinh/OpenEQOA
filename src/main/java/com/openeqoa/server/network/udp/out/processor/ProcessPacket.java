package com.openeqoa.server.network.udp.out.processor;

import com.openeqoa.server.network.udp.out.packet.ServerPacket;

public interface ProcessPacket {
    void setBuilder(ServerPacket.Builder builder);
}
