package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.MainEngine
import com.inkostilation.pong.server.engine.StarterEngine

class InitCommand: AbstractRequestCommand<AbstractEngine>() {
    override fun getEngineType() = StarterEngine::class.java

    override fun execute(input: AbstractEngine): Array<AbstractResponseCommand<*>> {
        input.redirect(MainEngine::class.java)
        return emptyArray()
    }
}