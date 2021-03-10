package com.inkostilation.pong.server.network

import com.inkostilation.pong.processing.IStateFul

interface IProcessor<M>: IStateFul {
    fun start(router: AbstractCommandRouter<M>)
    fun processConnection()
    fun stop()
}