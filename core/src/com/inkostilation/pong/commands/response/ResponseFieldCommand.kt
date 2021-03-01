package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.Field

class ResponseFieldCommand(private val field: Field) : AbstractResponseCommand() {
    override fun execute() {
        notifier.notifyObservers(field)
        notifier.notifyObservers(field.paddle1)
        notifier.notifyObservers(field.paddle2)
        notifier.notifyObservers(field.ball)
    }

}