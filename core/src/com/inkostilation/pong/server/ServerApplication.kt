package com.inkostilation.pong.server

import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.processing.IStateFul
import com.inkostilation.pong.server.network.*
import java.nio.channels.SocketChannel
import java.util.*
import java.util.function.Consumer

class ServerApplication private constructor(){
    private val processors: MutableList<AbstractProcessor<SocketChannel>> = ArrayList()
    private var router: AbstractCommandRouter<SocketChannel>? = null
    private val engines: MutableList<IEngine<SocketChannel>> = ArrayList()
    private lateinit var redirect: Redirect<SocketChannel>
    private var defaultEngine: Class<out IEngine<SocketChannel>>? = null
    private var started = false


    fun start() {
        redirect = Redirect(router!!)
        router!!.start(engines, defaultEngine!!)
        processors.forEach { it.start(router!!) }
        engines.forEach {
            it.start(redirect)
        }
        started = true
        loop()
    }

    private fun loop() {
        while (started) {
            processors.forEach(Consumer { processor: AbstractProcessor<SocketChannel> ->
                if (processor.state == IStateFul.State.NOT_STARTED) {
                    processor.start(router!!)
                }
                processor.processConnection()
            })
            engines.forEach { engine -> engine.act(Time.deltaTime) }
            Time.updateTime()
        }
    }

    class Builder {
        private val serverApplication = ServerApplication()

        fun build(defaultEngine: Class<out IEngine<*>>): ServerApplication {
            if (serverApplication.router == null) {
                serverApplication.router = StandardCommandRouter()
            }
            if (serverApplication.processors.size == 0) {
                serverApplication.processors.add(NetworkProcessor("0.0.0.0", 8080))
            }
            serverApplication.defaultEngine = defaultEngine as Class<out IEngine<SocketChannel>>
            return serverApplication
        }

        fun addEngine(engine: IEngine<SocketChannel>): Builder {
            serverApplication.engines.add(engine)
            return this
        }

        fun addEngines(engines: Array<IEngine<SocketChannel>>): Builder {
            serverApplication.engines.addAll(engines)
            return this
        }

        fun addProcessor(processor: AbstractProcessor<SocketChannel>): Builder {
            serverApplication.processors.add(processor)
            return this
        }

        fun addProcessors(processors: Array<AbstractProcessor<SocketChannel>>): Builder {
            serverApplication.processors.addAll(processors)
            return this
        }

        fun setRouter(router: AbstractCommandRouter<SocketChannel>): Builder {
            serverApplication.router = router
            return this
        }
    }
}