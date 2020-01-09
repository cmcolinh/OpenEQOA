package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x0000
 * 
 * @author colin
 *
 */
public final class GameVersionMessage extends AbstractPacketWrappingClientMessage {
    public GameVersionMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
        super(wrappedPacketBytes, startIndex, length);
    }

    /** get the version of EQOA the client is running */
    public int getGameVersion() {
        int gameVersion = wrappedPacketBytes[startIndex + 5];
        gameVersion = (gameVersion << 8) | wrappedPacketBytes[startIndex + 4];
        gameVersion = (gameVersion << 8) | wrappedPacketBytes[startIndex + 3];
        gameVersion = (gameVersion << 8) | wrappedPacketBytes[startIndex + 2];

        return gameVersion;
    }

    @Override
    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }
}
