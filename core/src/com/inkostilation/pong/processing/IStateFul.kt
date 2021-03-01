package com.inkostilation.pong.processing

interface IStateFul {
    enum class State {
        NOT_STARTED, STOPPED, STARTED, ERROR
    }
    fun start()
    val state: State
    fun stop()
}