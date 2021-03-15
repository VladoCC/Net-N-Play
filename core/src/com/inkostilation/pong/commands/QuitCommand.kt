package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine

class QuitCommand() : AbstractRequestCommand<AbstractEngine>() {
    override fun execute(input: AbstractEngine): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = AbstractEngine::class.java
}