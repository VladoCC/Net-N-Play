package com.inkostilation.pong.commands.request;

import com.inkostilation.pong.commands.AbstractPongCommand;
import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class RequestConnectionCommand<M> extends AbstractPongCommand<M> {

    private int index;

    public RequestConnectionCommand(int index) {
        this.index = index;
    }

    @Override
    protected void execute(IPongEngine<M> engine) throws IOException {
        engine.connectToGame(getMarker(), index);
    }
}
