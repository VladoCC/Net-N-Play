package com.inkostilation.pong.server.network

import com.inkostilation.pong.processing.IStateFul
import java.util.*

interface IProcessor: IStateFul {
    fun start(router: AbstractCommandRouter)
    fun processConnection()
    fun closeConnection(marker: UUID)
    fun stop()
}