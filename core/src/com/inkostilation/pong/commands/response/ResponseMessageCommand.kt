package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand

class ResponseMessageCommand(private val text: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        println(text)
    }

}