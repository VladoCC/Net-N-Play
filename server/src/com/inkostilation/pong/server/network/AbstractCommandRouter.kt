package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import kotlin.jvm.Throws

abstract class AbstractCommandRouter<M>: ICommandRouter<M> {

    override fun processCommand(command: AbstractRequestCommand<IEngine<M>, M>, marker: M): List<AbstractResponseCommand<*>> {
        if (command is QuitCommand) {
            onQuitCommand(marker)
            return emptyList()
        } else {
            return route(command)
        }
    }

    protected abstract fun onQuitCommand(marker: M)

    @Throws(IOException::class, NoEngineException::class)
    protected abstract fun route(command: AbstractRequestCommand<IEngine<M>, M>): List<AbstractResponseCommand<*>>

    protected abstract fun getEngine(marker: M): IEngine<M>
}