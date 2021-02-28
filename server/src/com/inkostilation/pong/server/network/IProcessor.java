package com.inkostilation.pong.server.network;

import com.inkostilation.pong.commands.AbstractResponseCommand;

import java.nio.channels.SocketChannel;

public interface IProcessor {

    void start();
    void stop();
    void processConnections();
    void sendMessage(AbstractResponseCommand command, SocketChannel channel);
}
