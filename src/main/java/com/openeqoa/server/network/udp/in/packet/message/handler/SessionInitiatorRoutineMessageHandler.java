package com.openeqoa.server.network.udp.in.packet.message.handler;

import com.openeqoa.server.network.client.UDPClientManager;
import com.openeqoa.server.network.udp.CalculateCRC;
import com.openeqoa.server.network.udp.UDPConnection;
import com.openeqoa.server.network.udp.in.packet.message.GameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.UserInformationMessage;
import com.openeqoa.server.network.udp.out.packet.message.GameVersionMessageBuilder;
import com.openeqoa.server.network.udp.out.processor.ProcessLoginPacket;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SessionInitiatorRoutineMessageHandler implements MessageHandler {
    public static final int FRONTIERS = 0x25;
    public static final short UNKNOWN_SERVER_ID = (short) 0xFFFE;
    public final UDPConnection udpConnection;
    public final UDPClientManager udpClientManager;
    public final short serverId;
    public final CalculateCRC calculateCRC;

    @Override
    public void visit(GameVersionMessage message, ProcessPacket processPacket) {
        if (message.getGameVersion() != FRONTIERS) {
            throw new IllegalArgumentException(
                    "The client is not identifying as a EQOA:Frontiers disc.  Only EQOA: Frontiers is supported");
        }
    }

    @Override
    public void visit(UserInformationMessage message, ProcessPacket processPacket) {
        short clientId = message.clientId();
        ((ProcessLoginPacket) processPacket).userName(message.userName())
                .serverId(serverId)
                .clientId(clientId)
                .message(message)
                .outMessage(buildGameVersionMessage(serverId, clientId, message));
    }

    private GameVersionMessageBuilder buildGameVersionMessage(short serverId, short clientId,
            UserInformationMessage message) {
        return new GameVersionMessageBuilder().messageNum(1);
    }
}
