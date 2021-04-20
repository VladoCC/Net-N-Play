package com.netnplay.commands

import com.netnplay.server.engine.AbstractEngine

/**
 * Special type of request command, which isn't designed to be executed on the server, but intended to act
 *  as a marker to notify server about clients decision to disconnect from it.
 */
class QuitCommand() : AbstractRequestCommand<AbstractEngine>() {
    override fun execute(input: AbstractEngine): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = AbstractEngine::class.java
}