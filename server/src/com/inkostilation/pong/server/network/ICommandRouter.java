package com.inkostilation.pong.server.network;

import com.inkostilation.pong.commands.AbstractRequestCommand;
import com.inkostilation.pong.engine.IEngine;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public interface ICommandRouter<M> {

    void route(AbstractRequestCommand<IEngine<M>, M> command) throws IOException, NoEngineException;
}
