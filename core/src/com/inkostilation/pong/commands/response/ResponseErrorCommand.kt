package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand

class ResponseErrorCommand(private val text: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        System.err.println("Server error: $text")
    }

}