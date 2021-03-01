package com.inkostilation.pong.server.application

import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.server.network.ICommandRouter
import com.inkostilation.pong.server.network.IProcessor
import com.inkostilation.pong.server.network.NetworkProcessor
import com.inkostilation.pong.server.network.StandardCommandRouter
import java.nio.channels.SocketChannel
import java.util.*
import java.util.function.Consumer

class ServerApplication private constructor(){
    private val processors: MutableList<IProcessor<SocketChannel>> = ArrayList()
    private var router: ICommandRouter<SocketChannel>? = null
    private val engines: MutableList<IEngine<SocketChannel>> = ArrayList()
    private var started = false

    fun start() {
        router!!.setEngines(engines)
        processors.forEach { it.start(router!!) }
        started = true
        loop()
    }

    private fun loop() {
        while (started) {
            processors.forEach(Consumer { processor: IProcessor<SocketChannel> ->
                if (processor.state == IProcessor.State.NOT_STARTED) {
                    processor.start(router!!)
                }
                processor.processConnections()
            })
            engines.forEach { engine -> engine.act(Time.deltaTime) }
            Time.updateTime()
        }
    }

    class Builder {
        val serverApplication = ServerApplication()

        fun build(): ServerApplication {
            if (serverApplication.router == null) {
                serverApplication.router = StandardCommandRouter()
            }
            if (serverApplication.processors.size == 0) {
                serverApplication.processors.add(NetworkProcessor("0.0.0.0", 8080))
            }
            return serverApplication
        }

        fun addEngine(engine: IEngine<SocketChannel>) {
            serverApplication.engines.add(engine)
        }

        fun addEngines(engines: Array<IEngine<SocketChannel>>) {
            serverApplication.engines.addAll(engines)
        }

        fun addProcessor(processor: IProcessor<SocketChannel>) {
            serverApplication.processors.add(processor)
        }

        fun addProcessors(processors: Array<IProcessor<SocketChannel>>) {
            serverApplication.processors.addAll(processors)
        }

        fun setRouter(router: ICommandRouter<SocketChannel>) {
            serverApplication.router = router
        }
    }
}