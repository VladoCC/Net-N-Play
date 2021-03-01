package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.PongGame
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException

class ResponseGameListCommand(var games: Array<PongGame>) : AbstractResponseCommand() {
    @Throws(IOException::class, NoEngineException::class)
    override fun execute() {
        notifier.notifyObservers(*games)
    }

}