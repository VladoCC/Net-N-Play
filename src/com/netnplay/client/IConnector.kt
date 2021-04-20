package com.netnplay.client

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.processing.IStateFul

/**
 * Interfaces representing object responsible for sending/receiving
 * commands from the server
 */
interface IConnector<I>: IStateFul {
    /**
     * Initialization method for connector object
     */
    fun start()

    /**
     * Sends [command] to the server.
     */
    fun send(command: AbstractRequestCommand<*>)

    /**
     * Method responsible for receiving commands sent by server.
     * Returns all the commands sent by server from the last call
     * in the list.
     */
    fun receive(): List<AbstractResponseCommand<I>>

    /**
     * This method is intended for disposing any resources used
     * by connector after the finish of this connectors lifecycle.
     */
    fun stop()
}