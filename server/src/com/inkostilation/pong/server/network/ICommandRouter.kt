package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.IEngine

interface ICommandRouter<M> {
    fun processCommand(command: AbstractRequestCommand<IEngine<M>, M>, marker: M): List<AbstractResponseCommand<*>>
    fun start(engines: List<IEngine<M>>)
    fun reroute(marker: M, engine: Class<out IEngine<M>>): Boolean
}