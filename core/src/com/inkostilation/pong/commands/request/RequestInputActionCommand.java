package com.inkostilation.pong.commands.request;

import com.inkostilation.pong.commands.AbstractPongCommand;
import com.inkostilation.pong.engine.Direction;
import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class RequestInputActionCommand<M> extends AbstractPongCommand<M> {

    private Direction direction;

    public RequestInputActionCommand(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void execute(IPongEngine<M> engine) throws IOException {
        getEngine().applyInput(direction, getMarker());
    }
}
