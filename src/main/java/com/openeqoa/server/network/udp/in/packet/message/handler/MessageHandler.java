package com.openeqoa.server.network.udp.in.packet.message.handler;

import com.openeqoa.server.network.udp.in.packet.ClientPacket;
import com.openeqoa.server.network.udp.in.packet.message.CharacterCreateMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterDeleteMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterSelectMessage;
import com.openeqoa.server.network.udp.in.packet.message.CharacterViewMessage;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.GameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.PrepareItemOnHotbarRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.RollRequestMessage;
import com.openeqoa.server.network.udp.in.packet.message.SendReportMessage;
import com.openeqoa.server.network.udp.in.packet.message.UserInformationMessage;
import com.openeqoa.server.network.udp.out.processor.ProcessPacket;

public interface MessageHandler {
    /** handle all messages in this packet */
    default void handle(ClientPacket packet, ProcessPacket processPacket) {
        packet.messages().stream().forEach(m -> this.handle(m, processPacket));
    }

    /** take action based on the type of ClientMessage encountered */
    default void handle(ClientMessage message, ProcessPacket processPacket) {
        message.accept(this, processPacket);
    }

    /** Handle a GameVersionMessage (opcode 0x0000) */
    default void visit(GameVersionMessage message, ProcessPacket processPacket) {
    } // 0x0000 - Game Information (Client)

    /** Handle a SendReportMessage (opcode 0x0009) */
    default void visit(SendReportMessage message, ProcessPacket processPacket) {
    } // 0x0009 - Send Report (Client)

    /** Handle a PrepareItemOnHotbarRequestMessage (opcode 0x000A) */
    default void visit(PrepareItemOnHotbarRequestMessage message, ProcessPacket processPacket) {
    } // 0x000A - Prepare Item on Hotbar Request (client)

    /** Handle a CharacterSelectMessage (opcode 0x002A) */
    default void visit(CharacterSelectMessage message, ProcessPacket processPacket) {
    } // 0x002A - Character Select

    /** Handle a CharacterCreateMessage (opcode 0x002B) */
    default void visit(CharacterCreateMessage message, ProcessPacket processPacket) {
    } // 0x002B - Character Create

    /** Handle a CharacterViewMessage (opcode 0x002C) */
    default void visit(CharacterViewMessage message, ProcessPacket processPacket) {
    } // 0x002C - Character View

    /** Handle a CharacterDeleteMessage (opcode 0x002D) */
    default void visit(CharacterDeleteMessage message, ProcessPacket processPacket) {
    } // 0x002D - Character Delete

    /** Handle a RollRequestMessage (opcode 0x00C8) */
    default void visit(RollRequestMessage message, ProcessPacket processPacket) {
    } // 0x00C8 - Roll Request

    /** Handle a UserInformationMessage (opcode 0x0904) */
    default void visit(UserInformationMessage message, ProcessPacket processPacket) {
    } // 0x0904 - Username and More
}
