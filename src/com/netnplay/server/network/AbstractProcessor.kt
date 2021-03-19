package com.netnplay.server.network

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.engine.AbstractEngine
import com.netnplay.server.engine.NullEngine
import java.util.*

abstract class AbstractProcessor: IProcessor {
    private var router: AbstractCommandRouter = object: AbstractCommandRouter() {
        override fun onQuitCommand(marker: UUID, processor: IProcessor) {}

        override fun start(engines: List<AbstractEngine>) {}

        override fun getEngine(marker: UUID): AbstractEngine = NullEngine()
        override fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine {return NullEngine()}

        override fun canReroute(marker: UUID, direction: Class<out AbstractEngine>) = false

        override fun stop() {}
    }

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
    protected abstract fun process(): List<UUID>
    protected abstract fun receive(marker: UUID): List<AbstractRequestCommand<AbstractEngine>>
    protected abstract fun send(command: List<AbstractResponseCommand<*>>, marker: UUID)

    final override fun start(router: AbstractCommandRouter) {
        this.router = router
        start()
    }

    protected abstract fun start()
}