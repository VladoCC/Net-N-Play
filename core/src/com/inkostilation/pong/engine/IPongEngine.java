package com.inkostilation.pong.engine;

import com.inkostilation.pong.commands.AbstractRequestCommand;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public interface IPongEngine<M> extends IEngine<M> {
    void sendCommand(AbstractRequestCommand<IEngine<M>, M>... command) throws IOException, NoEngineException;
    void sendFieldState(M marker) throws IOException;
    void sendPlayerRole(M marker) throws IOException;
    void sendScore(M marker) throws IOException;
    void applyInput(Direction direction, M marker) throws IOException;
    void startNewGame(M marker) throws IOException;
    void connectToGame(M marker, int index) throws IOException;
    void confirm(M Marker);
    void sendGameState(M marker) throws IOException;
    void sendGameList(M marker) throws IOException;
    void sendGameUpdate(M marker) throws IOException;
}
