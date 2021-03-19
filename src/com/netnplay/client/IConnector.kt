package com.netnplay.client

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.processing.IStateFul

interface IConnector<I>: IStateFul {
    fun start()
    fun send(command: AbstractRequestCommand<*>)
    fun receive(): List<AbstractResponseCommand<I>>
    fun stop()
}