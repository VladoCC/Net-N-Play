package com.netnplay.server.engine

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.network.Redirect
import java.util.*

/**
 * Interface for all of the engine classes, designed for sendboxing
 * command execution, protected access to resources through [Redirect] directives
 *
 * It also has [act] method to run on its of, performing some tasks,
 * while waiting for incoming commands. This logic gives engine
 * an ability to change its behavior over time or after some internal server events.
 */
interface IEngine {

    /**
     * Initialization method that can be used as resource initialization context.
     */
    fun start()

    /**
     * Method intended for any code that should be performed internally on the server
     * without commands from any client.
     */
    fun act(delta: Float)

    /**
     * Handshake method for client that was just redirected to this server.
     */
    fun enter(marker: UUID)

    /**
     * Method designed for command execution and all the tasks around it,
     * like redirecting and etc.
     * Receives [command] and returns array of [AbstractResponseCommand] that
     * this [command] returned.
     */
    fun process(command: AbstractRequestCommand<AbstractEngine>): Array<AbstractResponseCommand<*>>

    /**
     * This method is intended for releasing client-specific engine resources
     * and called when client is leaving this engine, either to be redirected
     * to other engine or to disconnect from the server.
     */
    fun quit(marker: UUID)

    /**
     * Getter for redirect directive.
     */
    fun getRedirect(): Redirect

    /**
     * Method for disposing any resources before the end of the engines lifecycle.
     */
    fun stop()
}