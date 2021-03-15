package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.NullEngine

class RequestMessageCommand(private val text: String): AbstractRequestCommand<AbstractEngine>() {
    override fun execute(input: AbstractEngine): Array<AbstractResponseCommand<*>> {
        println(text)
        return emptyArray()
    }

    override fun getEngineType() = NullEngine::class.java

}