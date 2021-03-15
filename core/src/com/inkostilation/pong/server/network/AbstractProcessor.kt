package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.NullEngine
import java.util.*

abstract class AbstractProcessor: IProcessor {
    private var router: AbstractCommandRouter = object: AbstractCommandRouter() {
        override fun onQuitCommand(marker: UUID, processor: IProcessor) {}

        override fun route(command: AbstractRequestCommand<AbstractEngine>, marker: UUID)
                = emptyList<AbstractResponseCommand<*>>()

        override fun start(engines: List<AbstractEngine>) {}

        override fun getEngine(marker: UUID): AbstractEngine = NullEngine()

        override fun reroute(marker: UUID, engine: Class<out AbstractEngine>) = true

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