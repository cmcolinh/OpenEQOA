package com.openeqoa.server.network.udp.out.packet.message;

import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
public class GameVersionMessageBuilder implements ServerMessage.Builder {
	private int messageNum;

	@Override
	public ServerMessage build() {
		return () -> new byte[] { (byte) 0xFB, (byte) 0x06, (byte) (messageNum & 0x0000FF),
				(byte) (messageNum << 8 & 0x0000FF), (byte) 0x00, (byte) 0x00, (byte) 0x25, (byte) 0x00, (byte) 0x00,
				(byte) 0x00 };
	}
}
