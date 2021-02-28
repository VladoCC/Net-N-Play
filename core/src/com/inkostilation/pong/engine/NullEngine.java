package com.inkostilation.pong.engine;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.commands.AbstractRequestCommand;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public class NullEngine<M> implements IEngine<M> {

    public static final NullEngine INSTANCE = new NullEngine();


    private NullEngine() {
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void receiveCommand(AbstractResponseCommand command, Object mark) throws IOException, NoEngineException {

    }

    @Override
    public void sendCommand(AbstractRequestCommand<IEngine<M>, M>... command) throws IOException, NoEngineException {

    }

    @Override
    public void quit(M marker) throws IOException {

    }
}
