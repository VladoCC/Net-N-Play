package com.netnplay.server.engine

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.network.Redirect
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