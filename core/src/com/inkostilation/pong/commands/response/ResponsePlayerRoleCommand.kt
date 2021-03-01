package com.inkostilation.pong.commands.response

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.PlayerRole

class ResponsePlayerRoleCommand(private val playerRole: PlayerRole) : AbstractResponseCommand() {
    override fun execute() {
        println(playerRole.toString())
    }

}