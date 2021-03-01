package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.exceptions.MarkerIsNotSet
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import kotlin.jvm.Throws
import kotlin.reflect.KClass

class StandardCommandRouter<M>(engines: List<IEngine<M>> = ArrayList()) : ICommandRouter<M> {
    private lateinit var engines: Map<KClass<out IEngine<M>>, IEngine<M>>

    init {
        setEngines(engines)
    }

    override fun setEngines(engines: List<IEngine<M>>) {
        this.engines = engines.map {e: IEngine<M> -> e::class to e}
                .toMap()
    }

    @Throws(IOException::class, NoEngineException::class)
    override fun route(command: AbstractRequestCommand<IEngine<M>, M>, processor: IProcessor<M>) {
        val type = command.engineType as KClass<out IEngine<M>>
        if (engines.containsKey(type)) {
            val engine = engines[type]
            val response = command.execute(engine!!)
                    .filterNotNull()
            for (abstractResponseCommand in response) {
                processor.sendMessage(
                        abstractResponseCommand,
                        command.marker?: throw MarkerIsNotSet()
                )
            }
        } else {
            processor.sendMessage(ResponseErrorCommand("No engine with class $type found on the server"), command.marker?: throw MarkerIsNotSet())
        }
    }
}