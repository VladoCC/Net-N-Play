package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException

class ResponseEmptyCommand : AbstractResponseCommand() {
    @Throws(IOException::class, NoEngineException::class)
    override fun execute() {
    }
}