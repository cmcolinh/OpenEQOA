package com.openeqoa.server.udp.in.parse_client_packet_bytes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.ParseClientPacketBytes;
import com.openeqoa.server.network.udp.in.packet.message.reliable.ClientReliableGameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.reliable.ClientReliableUserInformationMessage;
import com.openeqoa.server.udp.Packets;

/**
 * Packet 155 is 0x5ae7feffcfe0215ae70500200100fb060100000025000000fb3e0200040900030000000400000045514f410a0000006b69657368616573686101fa1069221cd445bcfd683c562287d970b71c12ae76c498fdf3ceeb444a0a49b5f6be24e4
 * @author H2597
 *
 */
class ClientPacket155Test {
    private ParseClientPacketBytes parseClientPacketBytes;

    public ClientPacket155Test() throws Exception {
        parseClientPacketBytes = new ParseClientPacketBytes.Implementation();
    }

    @Test
    @DisplayName("creates two reliable message objects")
    void createsTwoObjects() throws Exception {
        ClientPacket packet = parseClientPacketBytes.apply(Packets.getClientPacket(155));
        assertEquals(2, packet.reliableMessages().size());
    }

    @Test
    @DisplayName("first message is a ClientReliableGameVersionMessage")
    void firstMessageIsAGameVersionMessage() throws Exception {
    	ClientPacket packet = parseClientPacketBytes.apply(Packets.getClientPacket(155));
        assertEquals(ClientReliableGameVersionMessage.class, packet.reliableMessages().get(0).getClass());
    }

    @Test
    @DisplayName("second message is a ClientReliableUserInformationMessage")
    void secondMessageIsACharacterDeleteMessage() throws Exception {
    	ClientPacket packet = parseClientPacketBytes.apply(Packets.getClientPacket(155));
        assertEquals(ClientReliableUserInformationMessage.class, packet.reliableMessages().get(1).getClass());
    }
}
