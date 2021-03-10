package com.inkostilation.pong.server.engine

import com.inkostilation.pong.server.network.Redirect

interface IEngine<M> {
    fun start(redirect: Redirect<M>)
    fun act(delta: Float)
    fun stop(marker: M)
}