package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import java.nio.channels.SocketChannel
import kotlin.jvm.Throws

class StandardCommandRouter(engines: List<IEngine<SocketChannel>> = ArrayList()) : AbstractCommandRouter<SocketChannel>() {
    private lateinit var engines: Map<Class<out IEngine<SocketChannel>>, IEngine<SocketChannel>>

    init {
        start(engines)
    }

    override fun start(engines: List<IEngine<SocketChannel>>) {
        this.engines = engines.map {
                    e -> e::class.java to e
                }
                .toMap()
    }

    @Throws(IOException::class, NoEngineException::class)
    override fun route(command: AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>): List<AbstractResponseCommand<*>> {
        val type = command.getEngineType() as Class<out IEngine<SocketChannel>>
        if (engines.containsKey(type)) {
            val engine = engines[type]
            return command.execute(engine!!)
                    .toList()
        } else {
            return listOf(ResponseErrorCommand("No engine with class $type found on the server"))
        }
    }

    override fun onQuitCommand(marker: SocketChannel) {
        try {
            // todo engine.quit(marker!!)
            marker.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}