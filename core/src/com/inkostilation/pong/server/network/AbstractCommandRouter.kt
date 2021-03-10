package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import kotlin.jvm.Throws

abstract class AbstractCommandRouter<M>: ICommandRouter<M> {
    private lateinit var defaultEngine: Class<out IEngine<M>>

    final override fun start(engines: List<IEngine<M>>, defaultEngine: Class<out IEngine<M>>) {
        this.defaultEngine = defaultEngine
        start(engines)
    }

    protected abstract fun start(engines: List<IEngine<M>>)

    final override fun processCommand(command: AbstractRequestCommand<IEngine<M>, M>, marker: M): List<AbstractResponseCommand<*>> {
        if (getEngine(marker) == null) {
            reroute(marker, defaultEngine)
        }
        return if (command is QuitCommand) {
            onQuitCommand(marker)
            emptyList()
        } else {
            route(command, marker)
        }
    }

    protected abstract fun onQuitCommand(marker: M)

    @Throws(IOException::class, NoEngineException::class)
    protected abstract fun route(command: AbstractRequestCommand<IEngine<M>, M>, marker: M): List<AbstractResponseCommand<*>>

    protected abstract fun getEngine(marker: M): IEngine<M>?
}