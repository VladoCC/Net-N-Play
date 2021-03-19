package com.netnplay.commands

import com.netnplay.server.engine.AbstractEngine

class QuitCommand() : AbstractRequestCommand<AbstractEngine>() {
    override fun execute(input: AbstractEngine): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = AbstractEngine::class.java
}