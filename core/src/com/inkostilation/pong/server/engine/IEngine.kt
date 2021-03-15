package com.inkostilation.pong.server.engine

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.network.Redirect
import java.util.*

interface IEngine {

    fun start()
    fun act(delta: Float)

    fun enter(marker: UUID)
    fun process(command: AbstractRequestCommand<AbstractEngine>): Array<AbstractResponseCommand<*>>
    fun quit(marker: UUID)

    fun getRedirect(): Redirect

    fun stop()
}