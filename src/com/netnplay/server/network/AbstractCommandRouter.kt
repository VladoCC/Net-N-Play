package com.netnplay.server.network

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.commands.QuitCommand
import com.netnplay.commands.ResponseErrorCommand
import com.netnplay.server.engine.AbstractEngine
import com.netnplay.server.engine.IEngine
import java.util.*

/**
 * Standard abstraction for [ICommandRouter] that implements command processing features
 * and simple rules for clients to access specific engines, based on [reroute] functionality.
 */
abstract class AbstractCommandRouter: ICommandRouter {
    private lateinit var defaultEngine: Class<out AbstractEngine>

    /**
     * Initializes router and sets [defaultEngine] as an engine that sets to user on connect
     */
    final override fun start(engines: List<AbstractEngine>, defaultEngine: Class<out AbstractEngine>) {
        this.defaultEngine = defaultEngine
        start(engines)
    }

    /**
     * Utility method for initializing any necessary data at the start of the router,
     * for example, setting variables in engines.
     */
    protected abstract fun start(engines: List<AbstractEngine>)

    /**
     * This method checks if the client wants to leave the server and calls [onQuitCommand].
     * Otherwise it send received command to be checked for access and then be executed if allowed.
     */
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

    /**
     * Sends [command] to the engine, returned by [getEngine] with [marker] as parameter,
     * if [command] returns engine type that is equal to the engine corresponding to
     * the [marker] of the client that sent this command. After that it checks if command
     * contained a redirect call and sends it to [redirection].
     * Returns list of commands that was received from processing the [command]
     * through the engine.
     *
     * Ignores [command] otherwise and returns error message to be sent to the client.
     */
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

    /**
     * Method for redirecting clients from one engine to another.
     * [marker] should be the handle for client that is connected to the [engine].
     */
    private final fun redirection(marker: UUID, engine: IEngine) {
        val redirect = engine.getRedirect()
        if (getEngine(marker) == engine &&
                redirect.directed &&
                canReroute(marker, redirect.direction!!)) {
            engine.quit(marker)
            val goal = reroute(marker, engine.getRedirect().direction!!)
            goal.enter(marker)
        }
    }

    /**
     * Method that lets concrete realisations to build custom rules for accessing engines.
     */
    protected abstract fun canReroute(marker: UUID, direction: Class<out AbstractEngine>): Boolean

    /**
     * Method for disposing client-specific resources, when client disconnects from the server.
     * It's recommended to call [com.netnplay.server.engine.IEngine.quit] from here.
     */
    protected abstract fun onQuitCommand(marker: UUID, processor: IProcessor)

    /**
     * Returns engine that [marker] has access to.
     */
    protected abstract fun getEngine(marker: UUID): IEngine?
}