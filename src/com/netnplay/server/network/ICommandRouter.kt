package com.netnplay.server.network

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.engine.AbstractEngine
import java.util.*

interface ICommandRouter {
    fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine
    fun processCommand(command: AbstractRequestCommand<AbstractEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>>
    fun start(engines: List<AbstractEngine>, defaultEngine: Class<out AbstractEngine>)
    fun stop()
}