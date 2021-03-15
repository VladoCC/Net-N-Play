package com.inkostilation.pong.server

import com.inkostilation.pong.exceptions.NoDefaultClassEngineException
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.processing.IStateFul
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.network.*
import java.nio.channels.SocketChannel
import java.util.*
import java.util.function.Consumer

class ServerApplication private constructor(){
    private val processors: MutableList<AbstractProcessor> = ArrayList()
    private var router: AbstractCommandRouter? = null
    private val engines: MutableList<AbstractEngine> = ArrayList()
    private lateinit var redirect: Redirect
    private var defaultEngine: Class<out AbstractEngine>? = null
    private var started = false


    fun start() {
        router!!.start(engines, defaultEngine!!)
        processors.forEach { it.start(router!!) }
        started = true
        loop()
    }

    private fun loop() {
        while (started) {
            processors.forEach { processor: AbstractProcessor ->
                if (processor.state == IStateFul.State.NOT_STARTED) {
                    processor.start(router!!)
                }
                processor.processConnection()
            }
            engines.forEach { engine -> engine.act(Time.deltaTime) }
            Time.updateTime()
        }
    }

    class Builder {
        private val serverApplication = ServerApplication()

        fun build(defaultEngine: Class<out AbstractEngine>): ServerApplication {
            if (serverApplication.router == null) {
                serverApplication.router = StandardCommandRouter(emptyList())
                            { _,_ -> true}
            }
            if (serverApplication.processors.size == 0) {
                serverApplication.processors.add(NetworkProcessor("0.0.0.0", 8080))
            }
            serverApplication.defaultEngine = defaultEngine
            val hasEngine = serverApplication.engines
                    .filter { it::class.java == defaultEngine }
                    .isEmpty()
            if (hasEngine) {
                throw NoDefaultClassEngineException()
            }
            return serverApplication
        }

        fun addEngine(engine: AbstractEngine): Builder {
            serverApplication.engines.add(engine)
            return this
        }

        fun addEngines(engines: Array<AbstractEngine>): Builder {
            serverApplication.engines.addAll(engines)
            return this
        }

        fun addProcessor(processor: AbstractProcessor): Builder {
            serverApplication.processors.add(processor)
            return this
        }

        fun addProcessors(processors: Array<AbstractProcessor>): Builder {
            serverApplication.processors.addAll(processors)
            return this
        }

        fun setRouter(router: AbstractCommandRouter): Builder {
            serverApplication.router = router
            return this
        }
    }
}