package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import java.util.*
import kotlin.jvm.Throws

abstract class AbstractCommandRouter: ICommandRouter {
    private lateinit var defaultEngine: Class<out IEngine>

    final override fun start(engines: List<IEngine>, defaultEngine: Class<out IEngine>) {
        this.defaultEngine = defaultEngine
        start(engines)
    }

    protected abstract fun start(engines: List<IEngine>)

    final override fun processCommand(command: AbstractRequestCommand<IEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>> {
        if (getEngine(marker) == null) {
            reroute(marker, defaultEngine)
        }
        return if (command is QuitCommand) {
            onQuitCommand(marker, processor)
            emptyList()
        } else {
            route(command, marker)
        }
    }

    protected abstract fun onQuitCommand(marker: UUID, processor: IProcessor)

    @Throws(IOException::class, NoEngineException::class)
    protected abstract fun route(command: AbstractRequestCommand<IEngine>, marker: UUID): List<AbstractResponseCommand<*>>

    protected abstract fun getEngine(marker: UUID): IEngine?
}