package com.inkostilation.pong.commands.response;

import com.badlogic.gdx.Game;
import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.engine.PongGame;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public class ResponseGameListCommand extends AbstractResponseCommand {

    PongGame[] games;

    public ResponseGameListCommand(PongGame[] games) {
        this.games = games;
    }

    @Override
    public void execute() throws IOException, NoEngineException {
        getNotifier().notifyObservers(games);
    }
}
