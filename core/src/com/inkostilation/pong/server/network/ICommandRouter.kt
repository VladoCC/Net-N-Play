package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.IEngine
import java.util.*

interface ICommandRouter {
    fun processCommand(command: AbstractRequestCommand<IEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>>
    fun start(engines: List<IEngine>, defaultEngine: Class<out IEngine>)
    fun reroute(marker: UUID, engine: Class<out IEngine>): IEngine
    fun stop()
}