package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.IEngine

class QuitCommand() : AbstractRequestCommand<IEngine>() {
    override fun execute(engine: IEngine): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = IEngine::class.java
}