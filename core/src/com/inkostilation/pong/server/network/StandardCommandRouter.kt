package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import java.nio.channels.SocketChannel
import kotlin.jvm.Throws

class StandardCommandRouter(engines: List<IEngine<SocketChannel>> = ArrayList()) : AbstractCommandRouter<SocketChannel>() {
    private lateinit var engines: Map<Class<out IEngine<SocketChannel>>, IEngine<SocketChannel>>
    private lateinit var markedEngines: MutableMap<SocketChannel, Class<out IEngine<SocketChannel>>>

    init {
        start(engines)
    }

    override fun start(engines: List<IEngine<SocketChannel>>) {
        this.engines = engines.map {
                    e -> e::class.java to e
                }
                .toMap()
        markedEngines = mutableMapOf()
    }

    @Throws(IOException::class, NoEngineException::class)
    override fun route(command: AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>, marker: SocketChannel): List<AbstractResponseCommand<*>> {
        val type = command.getEngineType() as Class<out IEngine<SocketChannel>>
        if (markedEngines[marker] == type) {
            val engine = engines[type]
            return command.execute(engine!!)
                    .toList()
        } else {
            return listOf(ResponseErrorCommand("You don't have permission to interact with engine of class $type."))
        }
    }

    override fun onQuitCommand(marker: SocketChannel) {
        try {
            getEngine(marker)!!.stop(marker)
            marker.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getEngine(marker: SocketChannel): IEngine<SocketChannel>? {
        return if (markedEngines.containsKey(marker)) {
            engines[markedEngines[marker]]
        } else {
            null
        }
    }

    override fun reroute(marker: SocketChannel, engine: Class<out IEngine<SocketChannel>>): Boolean {
        markedEngines[marker] = engine
        return true
    }

    override fun stop() {
    }
}