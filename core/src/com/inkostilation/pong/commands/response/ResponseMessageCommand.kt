package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand

class ResponseMessageCommand(private val text: String) : AbstractResponseCommand<Void>() {
    override fun execute(input: Void) {
        println(text)
    }

}