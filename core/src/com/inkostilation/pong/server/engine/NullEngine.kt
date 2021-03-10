package com.inkostilation.pong.server.engine

import com.inkostilation.pong.server.network.Redirect
import java.io.IOException

class NullEngine<M>: IEngine<M> {
    override fun start(redirect: Redirect<M>) {}

    override fun act(delta: Float) {}

    @Throws(IOException::class)
    override fun stop(marker: M) {}
}