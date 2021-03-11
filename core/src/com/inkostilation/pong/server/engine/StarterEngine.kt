package com.inkostilation.pong.server.engine

import com.inkostilation.pong.server.network.Redirect
import java.nio.channels.SocketChannel
import java.util.*

class StarterEngine: AbstractEngine() {
    override fun prepare() {}

    override fun start() {}

    override fun act(delta: Float) {}

    override fun enter(marker: UUID) {}

    override fun quit(marker: UUID) {}

    override fun stop() {}
}