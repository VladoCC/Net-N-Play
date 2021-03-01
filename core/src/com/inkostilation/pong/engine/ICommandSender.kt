package com.inkostilation.pong.engine

import com.inkostilation.pong.commands.AbstractResponseCommand

interface ICommandSender {
    fun hasCommands(): Boolean
    fun getCommands(pool: MutableList<AbstractResponseCommand?>)
}