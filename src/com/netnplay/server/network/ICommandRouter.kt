package com.netnplay.server.network

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.engine.AbstractEngine
import java.util.*

/**
 * Main security and routing measure of the server.
 *
 * This class is designed to work as a connecting link
 * between [IProcessor] and [com.netnplay.server.engine.IEngine] that
 * received command should go to.
 */
interface ICommandRouter {
    /**
     * Reroutes client corresponding to [marker] to [engine].
     */
    fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine

    /**
     * Processes [command] recieved from client corresponding
     * to handle [marker] and sends it to [com.netnplay.server.engine.IEngine]
     * and then sends response to [processor].
     */
    fun processCommand(command: AbstractRequestCommand<AbstractEngine>, marker: UUID, processor: IProcessor): List<AbstractResponseCommand<*>>

    /**
     * Method for initializing resources at the start of the routers lifecycle.
     * Sets [defaultEngine] as a starting engine for the clients that are
     * just connected to the server.
     *
     */
    fun start(engines: List<AbstractEngine>, defaultEngine: Class<out AbstractEngine>)

    /**
     * Method for disposing any resources before the end of the engines lifecycle.
     */
    fun stop()
}