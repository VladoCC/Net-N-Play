package com.inkostilation.pong.commands.response;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.engine.GameState;

public class ResponseGameStateCommand extends AbstractResponseCommand {

    private GameState state;

    public ResponseGameStateCommand(GameState state) {
        this.state = state;
    }

    @Override
    public void execute() {
        getNotifier().notifyObservers(state);
    }
}