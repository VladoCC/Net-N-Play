package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseMessageCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.engine.NullEngine
import kotlin.reflect.KClass

class RequestMessageCommand<M>(private val text: String): AbstractRequestCommand<IEngine<M>, M>() {
    override fun execute(engine: IEngine<M>): Array<AbstractResponseCommand<*>> {
        println(text)
        return emptyArray()
    }

    override fun getEngineType() = NullEngine::class.java

}