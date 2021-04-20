package com.netnplay.commands

/**
 * Simple response command designed for testing purpose, which send [error] string to the client
 * and prints it using error output stream.
 */
class ResponseErrorCommand(private val error: String) : AbstractResponseCommand<Any>() {
    override fun execute(input: Any) {
        System.err.println("Server error: $error")
    }

}