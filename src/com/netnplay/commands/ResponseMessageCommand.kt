package com.netnplay.commands

import com.netnplay.commands.AbstractResponseCommand

class ResponseMessageCommand(private val text: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        println(text)
    }

}