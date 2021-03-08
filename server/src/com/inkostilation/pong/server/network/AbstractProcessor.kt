package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.engine.NullEngine
import com.inkostilation.pong.processing.IStateFul

abstract class AbstractProcessor<M>: IProcessor<M> {
    private var router: AbstractCommandRouter<M> = object: AbstractCommandRouter<M>() {
        override fun onQuitCommand(marker: M) {}

        override fun route(command: AbstractRequestCommand<IEngine<M>, M>)
                = emptyList<AbstractResponseCommand<*>>()

        override fun start(engines: List<IEngine<M>>) {}

        override fun getEngine(marker: M): IEngine<M> = NullEngine<M>()

        override fun reroute(marker: M, engine: Class<out IEngine<M>>): Boolean {
            TODO("Not yet implemented")
        }
    }

    override fun processConnection() {
        val markers = process()
        markers.forEach {marker ->
            val requests = receive(marker)

            val responses = mutableListOf<AbstractResponseCommand<*>>()
            requests.forEach { command ->
                responses.addAll(router.processCommand(command, marker))
            }

            send(responses, marker)
        }
    }
    protected abstract fun process(): List<M>
    protected abstract fun receive(marker: M): List<AbstractRequestCommand<IEngine<M>, M>>
    protected abstract fun send(command: List<AbstractResponseCommand<*>>, marker: M)

    override fun start(router: AbstractCommandRouter<M>) {
        this.router = router
        start()
    }

    protected abstract fun start()
}