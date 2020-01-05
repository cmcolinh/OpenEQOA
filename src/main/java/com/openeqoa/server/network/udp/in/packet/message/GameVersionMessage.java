package com.openeqoa.server.network.udp.in.packet.message;

import java.net.InetAddress;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;

/**
 * Model for client packets with opcode 0x0000
 * 
 * @author colin
 *
 */
public final class GameVersionMessage extends AbstractPacketWrappingClientMessage {
    public GameVersionMessage(InetAddress ipAddress, byte[] wrappedPacketBytes, int startIndex, int length) {
        super(ipAddress, wrappedPacketBytes, startIndex, length);
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
    public void accept(MessageHandler handler) {
        handler.visit(this);
    }
}
