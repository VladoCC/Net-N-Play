package com.inkostilation.pong.commands.request;

import com.inkostilation.pong.commands.AbstractPongCommand;
import com.inkostilation.pong.engine.IPongEngine;

import java.io.IOException;

public class RequestPlayerRoleCommand<M> extends AbstractPongCommand<M> {
    @Override
    public void execute(IPongEngine engine) throws IOException {
        getEngine().sendPlayerRole(getMarker());
    }
}
