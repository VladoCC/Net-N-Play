package com.netnplay.commands

import com.netnplay.server.engine.AbstractEngine

/**
 * Subtype of [ICommand], which represents
 * request (command sent from the client to the server, which expects to receive
 * a list of commands as an answer).
 *
 * Generic parameter [E], which is a subclass of [AbstractEngine] limits
 * input type of [execute] method to be an engine, which is intended to make a
 * sandbox layer for request command execution out of the engines.
 */
abstract class AbstractRequestCommand<E: AbstractEngine>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    /**
     * Method specific to [AbstractRequestCommand] and introduced for security response.
     * It let's [com.netnplay.server.network.ICommandRouter]'s to control access of different users to different
     * command/engine types and enforces usage of [AbstractEngine.redirect] requests.
     */
    abstract fun getEngineType(): Class<out AbstractEngine>
}