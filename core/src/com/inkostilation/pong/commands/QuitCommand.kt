package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.IEngine

class QuitCommand() : AbstractRequestCommand<IEngine<Any>, Any>() {
    override fun execute(engine: IEngine<Any>): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = IEngine::class.java
}