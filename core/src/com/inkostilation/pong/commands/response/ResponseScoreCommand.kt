package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.Score
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException

class ResponseScoreCommand(private val score: Score) : AbstractResponseCommand() {
    @Throws(IOException::class, NoEngineException::class)
    override fun execute() {
        notifier.notifyObservers(score)
        println("Score received")
    }

}