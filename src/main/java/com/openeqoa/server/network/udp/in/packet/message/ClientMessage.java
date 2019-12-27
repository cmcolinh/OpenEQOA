package com.openeqoa.server.network.udp.in.packet.message;

import com.openeqoa.server.network.udp.MessageHandler;

public interface ClientMessage {
    public void accept(MessageHandler handler);
}
