package com.inkostilation.pong.engine

interface IEngine<M> {
    fun act(delta: Float)
    fun quit(marker: M)
}