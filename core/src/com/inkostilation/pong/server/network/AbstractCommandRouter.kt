package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.AbstractEngine
import java.util.*
import kotlin.jvm.Throws

abstract class AbstractCommandRouter: ICommandRouter {
    private lateinit var defaultEngine: Class<out AbstractEngine>

    final override fun start(engines: List<AbstractEngine>, defaultEngine: Class<out AbstractEngine>) {
        this.defaultEngine = defaultEngine
        start(engines)
    }

    protected abstract fun start(engines: List<AbstractEngine>)

    final override fun processCommand(command: AbstractRequestCommand<AbstractEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>> {
        if (getEngine(marker) == null) {
            reroute(marker, defaultEngine)
        }
        return if (command is QuitCommand) {
            onQuitCommand(marker, processor)
            emptyList()
        } else {
            routeCommand(command, marker)
        }
    }

    private final fun routeCommand(command: AbstractRequestCommand<AbstractEngine>, marker: UUID): List<AbstractResponseCommand<*>> {
        val engine = getEngine(marker)
        return if (engine != null && command.getEngineType() == engine::class.java) {
            val response = engine.process(command)
                    .toList()

            redirection(marker, engine)

            response
        } else {
            listOf(ResponseErrorCommand("You don't have permission to interact with engine of class ${command.getEngineType()}."))
        }
    }

    private final fun redirection(marker: UUID, engine: IEngine) {
        val redirect = engine.getRedirect()
        if (redirect.directed && canReroute(marker, redirect.direction!!)) {
            engine.quit(marker)
            val goal = reroute(marker, engine.getRedirect().direction!!)
            goal.enter(marker)
        }
    }

    protected abstract fun canReroute(marker: UUID, direction: Class<out AbstractEngine>): Boolean

    protected abstract fun onQuitCommand(marker: UUID, processor: IProcessor)

    protected abstract fun getEngine(marker: UUID): IEngine?
}