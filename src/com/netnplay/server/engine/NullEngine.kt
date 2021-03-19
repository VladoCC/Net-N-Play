package com.netnplay.server.engine

import java.io.IOException
import java.util.*

class NullEngine: AbstractEngine() {
    override fun prepare() {}

    override fun start() {}

    override fun act(delta: Float) {}
    override fun enter(marker: UUID) {}

    override fun quit(marker: UUID) {}

    @Throws(IOException::class)
    override fun stop() {}
}