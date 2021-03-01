package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IPongEngine

abstract class AbstractPongCommand<M> : AbstractRequestCommand<IPongEngine<M>?, M>()