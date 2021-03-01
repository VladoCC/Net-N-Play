package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand

class ResponseErrorCommand(private val text: String) : AbstractResponseCommand<Void>() {
    override fun execute(input: Void) {
        System.err.println("Server error: $text")
    }

}