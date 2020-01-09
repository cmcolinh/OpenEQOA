package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

/**
 * Model for client packets with opcode 0x002B
 * 
 * @author colin
 *
 */
public final class CharacterCreateMessage extends AbstractPacketWrappingClientMessage {
    private int nameLength = 0;

    public CharacterCreateMessage(byte[] wrappedPacketBytes, int startIndex, int length) {
        super(wrappedPacketBytes, startIndex, length);
    }

    @Override
    public void accept(MessageHandler handler, ProcessPacket processPacket) {
        handler.visit(this, processPacket);
    }

    /** @return the length of the selected name */
    public int getNameLength() {
        return nameLength > 0 ? nameLength
                : wrappedPacketBytes[startIndex + 5] << 24 + wrappedPacketBytes[startIndex + 4] << 16
                        + wrappedPacketBytes[startIndex + 3] << 8 + wrappedPacketBytes[startIndex + 2];
    }

    /** @return the selected player name */
    public String getName() {
        String name = "";
        for (int index = startIndex + 6; index < startIndex + 6 + getNameLength(); index++) {
            name += wrappedPacketBytes[index];
        }
        return name;
    }

    /** @return the player's level (should be 1, right?) */
    public byte getPlayerLevel() {
        // packets store level as double it's actual value, so will divide by 2
        return (byte) (wrappedPacketBytes[startIndex + getNameLength() + 6] / 2);
    }

    /** @return the selected race */
    public byte getPlayerRace() {
        return wrappedPacketBytes[startIndex + getNameLength() + 7];
    }

    /** @return the selected class */
    public byte getPlayerClass() {
        return wrappedPacketBytes[startIndex + getNameLength() + 8];
    }

    /** @return the selected gender (internally, 0x00 is male and 0x02 is female) */
    public byte getPlayerGender() {
        return wrappedPacketBytes[startIndex + getNameLength() + 9];
    }

    /** @return the selected hair color */
    public byte getPlayerHairColor() {
        return wrappedPacketBytes[startIndex + getNameLength() + 10];
    }

    /** @return the selected hair style */
    public byte getPlayerHairStyle() {
        return wrappedPacketBytes[startIndex + getNameLength() + 11];
    }

    /** @return the selected hair length */
    public byte getPlayerHairLength() {
        return wrappedPacketBytes[startIndex + getNameLength() + 12];
    }

    /** @return the selected face */
    public byte getPlayerFace() {
        return wrappedPacketBytes[startIndex + getNameLength() + 12];
    }
}
