package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.processing.IStateFul

interface IProcessor<M>: IStateFul {
    var router: ICommandRouter<M>
    fun processConnections()
    fun sendMessage(command: AbstractResponseCommand<*>, channel: M)

    fun start(router: ICommandRouter<M>) {
        this.router = router
        start()
    }
}