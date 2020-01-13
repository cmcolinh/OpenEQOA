package com.openeqoa.server.network.udp.out.processor;

import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.out.packet.GameVersionPacket;
import com.openeqoa.server.network.udp.out.packet.ServerPacket;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true, chain = true)
@Setter
@Getter
@RequiredArgsConstructor
public class ProcessLoginPacket implements ProcessPacket {
    private final CalculateCRC calculateCRC;

    private ClientMessage message;

    private GameVersionMessageBuilder outMessage;

    private ServerPacket.Builder builder; // the packet that will be scheduled to be sent, or null if no response

    private String userName;

    private int packetNum = 1;

    private short clientId;

    private short serverId;

    public ServerPacket.Builder getBuilder() {
        if (builder == null) {
            builder = new GameVersionPacket.Builder(calculateCRC).clientId(clientId)
                    .sessionId(message.sessionId())
                    .message(outMessage)
                    .currentBundle(1) // TODO: Get real numbers
                    .lastBundle(1) // TODO: calculate real numbers
                    .lastMessage(2); // TODO: calculate real numbers
        }
        return builder;
    }
}
