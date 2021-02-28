package com.inkostilation.pong.commands.request;

import com.inkostilation.pong.commands.AbstractPongCommand;
import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class RequestGameListCommand<M> extends AbstractPongCommand<M> {
    @Override
    protected void execute(IPongEngine<M> engine) throws IOException {
        engine.sendGameList(getMarker());
    }
}
