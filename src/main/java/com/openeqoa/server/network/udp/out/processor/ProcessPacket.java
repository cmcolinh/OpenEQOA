package com.openeqoa.server.network.udp.out.processor;

import com.openeqoa.server.network.udp.out.packet.ServerPacket;

public interface ProcessPacket {
    ServerPacket.Builder getBuilder();

    /** packet number in sequence in this session from the server */
    int packetNum();

    /** two byte client endpoint id */
    short clientId();

    /** two byte server endpoint id */
    short serverId();
}
