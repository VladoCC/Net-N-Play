package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseMessageCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.server.engine.MainEngine
import com.inkostilation.pong.server.engine.NullEngine

class CounterCommand(): AbstractRequestCommand<MainEngine>() {
    override fun execute(input: MainEngine): Array<AbstractResponseCommand<*>> {
        val counter = input.counter
        println("This command was called $counter times")
        return arrayOf(ResponseMessageCommand("Counter is $counter"))
    }

    override fun getEngineType() = MainEngine::class.java

}