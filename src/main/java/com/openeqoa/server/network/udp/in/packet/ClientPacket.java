package com.openeqoa.server.network.udp.in.packet;

import java.util.List;

import com.openeqoa.server.network.udp.in.packet.message.reliable.ClientReliableMessage;

public interface ClientPacket {

	List<ClientReliableMessage> reliableMessages();
	
    //@AllArgsConstructor
    //@Accessors(fluent = true, chain = true)
    //public static class Implementation implements ClientPacket {


    	
    //    private final byte[] packetBytes;
        
    //}

}
