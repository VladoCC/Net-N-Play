package com.netnplay.server.network

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.engine.AbstractEngine
import com.netnplay.server.engine.NullEngine
import java.util.*

/**
 * Implementations of the main behaviors of [IProcessor], designed to
 * simplify creation of concrete realizations of processors.
 */
abstract class AbstractProcessor: IProcessor {
    private var router: AbstractCommandRouter = object: AbstractCommandRouter() {
        override fun onQuitCommand(marker: UUID, processor: IProcessor) {}

        override fun start(engines: List<AbstractEngine>) {}

        override fun getEngine(marker: UUID): AbstractEngine = NullEngine()
        override fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine {return NullEngine()}

        override fun canReroute(marker: UUID, direction: Class<out AbstractEngine>) = false

        override fun stop() {}
    }

    /**
     * Implementation that distributes connection processing
     * to three different steps.
     */
    final override fun processConnection() {
        val markers = process()
        markers.forEach {marker ->
            val requests = receive(marker)

            val responses = mutableListOf<AbstractResponseCommand<*>>()
            requests.forEach { command ->
                responses.addAll(router.processCommand(command, marker, this))
            }

            send(responses, marker)
        }
    }

    /**
     * Returns list of handles of clients that are ready to send data.
     */
    protected abstract fun process(): List<UUID>

    /**
     * Returns list of commands, received from the client corresponding to [marker].
     */
    protected abstract fun receive(marker: UUID): List<AbstractRequestCommand<AbstractEngine>>

    /**
     * Sends list [command] to the client corresponding to [marker].
     */
    protected abstract fun send(commands: List<AbstractResponseCommand<*>>, marker: UUID)

    /**
     * Initializes router and then calls [start].
     */
    final override fun start(router: AbstractCommandRouter) {
        this.router = router
        start()
    }

    protected abstract fun start()
}