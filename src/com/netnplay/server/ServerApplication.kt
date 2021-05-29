package com.netnplay.server

import com.netnplay.exceptions.NoDefaultClassEngineException
import com.netnplay.processing.IStateFul
import com.netnplay.server.engine.AbstractEngine
import com.netnplay.server.network.*
import java.util.*


/**
 * Main class for a server.
 * It lets you set up your applications, equip it with all the objects
 * that it needs to correctly interact with clients.
 *
 * Call [start] method to run this application.
 * This method is blocking.
 */
class ServerApplication private constructor(){
    private val processors: MutableList<AbstractProcessor> = ArrayList()
    private var router: AbstractCommandRouter? = null
    private val engines: MutableList<AbstractEngine> = ArrayList()
    private lateinit var redirect: Redirect
    private var defaultEngine: Class<out AbstractEngine>? = null
    private var started = false

    /**
     * Blocking method that initializes and starts server.
     * After that it will run this servers configuration.
     */
    fun start() {
        router!!.start(engines, defaultEngine!!)
        processors.forEach { it.start(router!!) }
        started = true
        loop()
    }

    /**
     * Main body of the server application.
     * Method that loops through all of the processors, engines
     * and runs received commands through router.
     */
    private fun loop() {
        while (started) {
            processors.forEach { processor: AbstractProcessor ->
                if (processor.getState() == IStateFul.State.NOT_STARTED) {
                    processor.start(router!!)
                }
                processor.processConnection()
            }
            engines.forEach { engine -> engine.act(Time.deltaTime) }
            Time.updateTime()
        }
    }

    /**
     * Builder pattern for server application that simplifies
     * initialization of the applications by chaining methods for
     * adding objects to the server.
     */
    class Builder {
        private val serverApplication = ServerApplication()

        /**
         * Returns constructed [ServerApplication]. Also fills all of the
         * empty fields with default values.
         */
        fun build(defaultEngine: Class<out AbstractEngine>): ServerApplication {
            if (serverApplication.router == null) {
                serverApplication.router = StandardCommandRouter()
                            { _, _ -> true}
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