package com.netnplay.server.network

import com.netnplay.processing.IStateFul
import java.util.*

/**
 * Class intended for performing client-server interactions:
 * receive data from clients, process it and respond back
 * to the client.
 * It works in the way similar to RESTful apis.
 */
interface IProcessor: IStateFul {
    /**
     * Initialization method of the processor.
     * Parameter [router] corresponds to the [AbstractCommandRouter],
     * which should be used for sending commands received by
     * the processor to the server and sending response back to
     * the client that sent a request.
     */
    fun start(router: AbstractCommandRouter)

    /**
     * Method responsible for the full cycle of processing of
     * the connections. Starting with checking for incoming data and
     * ending with responding to clients.
     */
    fun processConnection()

    /**
     * Closes connection with client, corresponding
     * to the handle [marker].
     *
     * It can be used for disposing any client-specific resources.
     * And, for example, closing socket connections, if client wants
     * to disconnect, to make sure that processor won't check
     * the socket anymore.
     */
    fun closeConnection(marker: UUID)

    /**
     * Method for disposing any resources before the end of the processors lifecycle.
     */
    fun stop()
}