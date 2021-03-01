package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.GameState

class ResponseGameStateCommand(private val state: GameState) : AbstractResponseCommand() {
    override fun execute() {
        notifier.notifyObservers(state)
    }

}