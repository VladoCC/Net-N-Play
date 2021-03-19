package com.netnplay.server.network

import com.netnplay.processing.IStateFul
import java.util.*

interface IProcessor: IStateFul {
    fun start(router: AbstractCommandRouter)
    fun processConnection()
    fun closeConnection(marker: UUID)
    fun stop()
}