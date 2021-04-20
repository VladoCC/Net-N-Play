package com.netnplay.commands

/**
 * Simple response command designed for testing purpose, which send [text] string to the client
 * and prints it using standard output stream.
 */
class ResponseMessageCommand(private val text: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        println(text)
    }

}