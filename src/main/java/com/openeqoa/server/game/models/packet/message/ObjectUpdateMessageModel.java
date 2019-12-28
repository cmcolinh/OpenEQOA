package com.openeqoa.server.game.models.packet.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ObjectUpdateMessageModel {
    @Getter
	private byte[] message;

    public int getInstanceId() {
    	return ((0xFF & message[0]) << 24) | ((0xFF & message[1]) << 16) |
        ((0xFF & message[2]) << 8) | (0xFF & message[3]);
    }

    public double getX() {
    	int rawCoordinates = ((0xFF & message[5]) << 16) | ((0xFF & message[6]) << 8) | (0xFF & message[7]);
    	return rawCoordinates / 128.0;
    }

    public double getZ() {
    	int rawCoordinates = ((0xFF & message[8]) << 16) | ((0xFF & message[9]) << 8) | (0xFF & message[10]);
    	return rawCoordinates / 128.0;
    }

    public double getY() {
    	int rawCoordinates = ((0xFF & message[11]) << 16) | ((0xFF & message[12]) << 8) | (0xFF & message[13]);
    	return rawCoordinates / 128.0;
    }
}

