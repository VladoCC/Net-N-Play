package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand

class ResponseObjectCommand(private val `object`: Any) : AbstractResponseCommand() {
    override fun execute() {
        println(`object`.toString())
    }

}