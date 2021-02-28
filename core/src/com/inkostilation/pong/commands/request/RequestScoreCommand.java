package com.inkostilation.pong.commands.request;

import com.inkostilation.pong.commands.AbstractPongCommand;
import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class RequestScoreCommand<M> extends AbstractPongCommand<M> {
    @Override
    public void execute(IPongEngine<M> engine) throws IOException {
        engine.sendScore(getMarker());
    }
}
