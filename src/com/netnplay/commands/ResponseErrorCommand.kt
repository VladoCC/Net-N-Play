package com.netnplay.commands

import com.netnplay.commands.AbstractResponseCommand

class ResponseErrorCommand(private val text: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        System.err.println("Server error: $text")
    }

}