package com.inkostilation.pong.client

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.processing.IStateFul
import com.inkostilation.pong.server.engine.AbstractEngine

interface IConnector<I>: IStateFul {
    fun start()
    fun send(command: AbstractRequestCommand<*>)
    fun receive(): List<AbstractResponseCommand<I>>
    fun stop()
}