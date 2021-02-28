package com.inkostilation.pong.commands;

import com.inkostilation.pong.engine.IEngine;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class QuitCommand extends AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel> {
    @Override
    public void execute(IEngine<SocketChannel> engine) {
        try {
            getEngine().quit(getMarker());
            getMarker().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
