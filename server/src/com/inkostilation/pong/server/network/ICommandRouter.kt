package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import kotlin.jvm.Throws

interface ICommandRouter<M> {
    @Throws(IOException::class, NoEngineException::class)
    fun route(command: AbstractRequestCommand<IEngine<M>, M>, processor: IProcessor<M>)

    fun setEngines(engines: List<IEngine<M>>)
}