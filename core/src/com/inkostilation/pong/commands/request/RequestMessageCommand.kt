package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.NullEngine

class RequestMessageCommand<M>(private val text: String): AbstractRequestCommand<IEngine<M>, M>() {
    override fun execute(engine: IEngine<M>): Array<AbstractResponseCommand<*>> {
        println(text)
        return emptyArray()
    }

    override fun getEngineType() = NullEngine::class.java

}