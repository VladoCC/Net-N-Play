package com.inkostilation.pong.commands;

import com.inkostilation.pong.engine.IPongEngine;

public abstract class AbstractPongCommand<M> extends AbstractRequestCommand<IPongEngine<M>, M> {

}
