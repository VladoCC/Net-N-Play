package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine
import java.util.*

interface ICommandRouter {
    fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine
    fun processCommand(command: AbstractRequestCommand<AbstractEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>>
    fun start(engines: List<AbstractEngine>, defaultEngine: Class<out AbstractEngine>)
    fun stop()
}