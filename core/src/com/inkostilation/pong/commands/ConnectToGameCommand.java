package com.inkostilation.pong.commands;

import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class ConnectToGameCommand<M> extends AbstractPongCommand<M> {
    @Override
    public void execute(IPongEngine engine) throws IOException {
        getEngine().startNewGame(getMarker());
    }
}