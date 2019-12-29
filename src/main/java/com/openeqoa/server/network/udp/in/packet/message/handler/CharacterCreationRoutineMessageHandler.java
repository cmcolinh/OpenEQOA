package com.openeqoa.server.network.udp.in.packet.message.handler;

import com.openeqoa.server.network.udp.in.packet.message.CharacterCreateMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterDeleteMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterSelectMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterViewMessage;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CharacterCreationRoutineMessageHandler implements MessageHandler {
	@Override
	public void visit(CharacterSelectMessage message) {
		// TODO some code specific to character selection
	}

	@Override
	public void visit(CharacterCreateMessage message) {
		// TODO some code specific to character creation
	}

	public void visit(CharacterViewMessage message) {
		// TODO some code specific to character viewing
	}

	public void visit(CharacterDeleteMessage message) {
		// TODO some code specific to character deletion
	}
}
