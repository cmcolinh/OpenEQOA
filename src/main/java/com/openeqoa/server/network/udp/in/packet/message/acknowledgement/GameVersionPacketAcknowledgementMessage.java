package com.openeqoa.server.network.udp.in.packet.message.acknowledgement;

import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Model for the acknowledgement of the server's GameVersionPacket
 *
 * @author colin
 *
 */
@AllArgsConstructor
@Getter
public class GameVersionPacketAcknowledgementMessage implements ClientMessage {
    private final short clientId;

    private final short serverId;

    private final int sessionId;

    @Override
    public void accept(MessageHandler handler) {
        handler.visit(this);
    }
}
