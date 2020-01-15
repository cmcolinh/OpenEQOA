package com.openeqoa.server.udp.in;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.openeqoa.server.network.udp.ProcessUDPEstablishConnectionPacket;
import com.openeqoa.server.network.udp.in.packet.message.ClientMessage;
import com.openeqoa.server.network.udp.in.packet.message.GameVersionMessage;
import com.openeqoa.server.network.udp.in.packet.message.UserInformationMessage;
import com.openeqoa.server.network.udp.in.packet.message.handler.MessageHandler;
import com.openeqoa.server.network.udp.in.packet.message.handler.SessionInitiatorRoutineMessageHandler;
import com.openeqoa.server.udp.MockUDPClientManager;

class ProcessFirstUDPPacketTest {
    private ProcessUDPEstablishConnectionPacket processUDPPacket;

    public ProcessFirstUDPPacketTest() throws Exception {
        byte[] samplePacketBytes = new byte[] { (byte) 0x5a, (byte) 0xe7, // client code
                (byte) 0xfe, (byte) 0xff, // server code
                (byte) 0xcf, // related to length
                (byte) 0xe0, // packet router code [e is initialize connection routine]
                (byte) 0x21, // session action (what does this mean anyway?)
                (byte) 0x5a, (byte) 0xe7, (byte) 0x05, (byte) 0x00, // session
                (byte) 0x20, // bundle type
                (byte) 0x01, (byte) 0x00, // Client Packet number
                (byte) 0xfb, // Message Separator
                (byte) 0x06, // Length of Message
                (byte) 0x01, (byte) 0x00, // Message Number in Packet
                (byte) 0x00, (byte) 0x00, // Message Opcode (00 00) = game version
                (byte) 0x25, (byte) 0x00, (byte) 0x00, (byte) 0x00, // Game version (Frontiers)
                (byte) 0xfb, // Message Separator
                (byte) 0x3e, // Message Length
                (byte) 0x02, (byte) 0x00, // Message Number in Packet
                (byte) 0x04, (byte) 0x09, // Message Opcode (09 04) = User information packet
                (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x45, (byte) 0x51, (byte) 0x4f, (byte) 0x41, (byte) 0x0A, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x6b, // k
                (byte) 0x69, (byte) 0x65, (byte) 0x73, (byte) 0x68, // iesh
                (byte) 0x61, (byte) 0x65, (byte) 0x73, (byte) 0x68, // aesh
                (byte) 0x61, (byte) 0x01, (byte) 0xfa, (byte) 0x10, // a
                (byte) 0x69, (byte) 0x22, (byte) 0x1c, (byte) 0xd4, (byte) 0x45, (byte) 0xbc, (byte) 0xfd, (byte) 0x68,
                (byte) 0x3c, (byte) 0x56, (byte) 0x22, (byte) 0x87, (byte) 0xd9, (byte) 0x70, (byte) 0xb7, (byte) 0x1c,
                (byte) 0x12, (byte) 0xae, (byte) 0x76, (byte) 0xc4, (byte) 0x98, (byte) 0xfd, (byte) 0xf3, (byte) 0xce,
                (byte) 0xeb, (byte) 0x44, (byte) 0x4a, (byte) 0x0a, (byte) 0x49, (byte) 0xb5, // end of message
                (byte) 0xf6, (byte) 0xbe, (byte) 0x24, (byte) 0x34 }; // CRC
        processUDPPacket = ProcessUDPEstablishConnectionPacket.withBytes(samplePacketBytes,
                InetAddress.getByName("localhost"), b -> new byte[4], new MockUDPClientManager());
    }

    @Test
    @DisplayName("creates two message objects")
    void createsTwoObjects() throws Exception {
        List<ClientMessage> messages = findMessages(processUDPPacket);
        assertEquals(2, messages.size());
    }

    @Test
    @DisplayName("first message is a GameVersionMessage")
    void firstMessageIsAGameVersionMessage() throws Exception {
        List<ClientMessage> messages = findMessages(processUDPPacket);
        assertEquals(GameVersionMessage.class, messages.get(0).getClass());
    }

    @Test
    @DisplayName("first message version number is Frontiers (0x00000025)")
    void firstMessageVersionIsFrontiers() throws Exception {
        List<ClientMessage> messages = findMessages(processUDPPacket);
        assertEquals(0x00000025, ((GameVersionMessage) messages.get(0)).getGameVersion());
    }

    @Test
    @DisplayName("second message is a UserInformatonMessage")
    void secondMessageIsACharacterDeleteMessage() throws Exception {
        List<ClientMessage> messages = findMessages(processUDPPacket);
        assertEquals(UserInformationMessage.class, messages.get(1).getClass());
    }

    @Test
    @DisplayName("messages will be handled by a SessionInitiatorRoutineMessageHandler")
    void handlerIsASessionInitiatorRoutineMessageHandler() throws Exception {
        MessageHandler handler = getHandler(processUDPPacket);
        assertEquals(SessionInitiatorRoutineMessageHandler.class,
                Optional.ofNullable(handler).map(Object::getClass).orElse(null));
    }

    @SuppressWarnings("unchecked")
    private List<ClientMessage> findMessages(ProcessUDPEstablishConnectionPacket processUDPPacket) throws Exception {
        Field field = processUDPPacket.getClass().getDeclaredField("packetBytes");
        field.setAccessible(true);

        byte[] packetBytes = (byte[]) field.get(processUDPPacket);

        field = processUDPPacket.getClass().getDeclaredField("ipAddress");
        field.setAccessible(true);

        Method m = processUDPPacket.getClass().getDeclaredMethod("findMessages", byte[].class);
        m.setAccessible(true);
        return (List<ClientMessage>) m.invoke(processUDPPacket, packetBytes);
    }

    private MessageHandler getHandler(ProcessUDPEstablishConnectionPacket processUDPPacket) throws Exception {
        Field field = processUDPPacket.getClass().getDeclaredField("packetBytes");
        field.setAccessible(true);

        byte[] packetBytes = (byte[]) field.get(processUDPPacket);

        Method m = processUDPPacket.getClass().getDeclaredMethod("getHandler", byte[].class);
        m.setAccessible(true);
        return (MessageHandler) m.invoke(processUDPPacket, packetBytes);
    }
}
