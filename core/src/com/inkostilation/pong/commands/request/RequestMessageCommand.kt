package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.engine.IEngine

class RequestMessageCommand(private val text: String) : AbstractRequestCommand<Any?, Any?>() {
    override fun execute(engine: IEngine<*>?) {
        println(text)
    }

}