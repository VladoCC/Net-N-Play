package com.inkostilation.pong.desktop.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.processing.IStateFul

interface IConnector<I>: IStateFul {
    fun send(command: AbstractRequestCommand<IEngine<*>, *>)
    fun receive(): List<AbstractResponseCommand<I>>
}