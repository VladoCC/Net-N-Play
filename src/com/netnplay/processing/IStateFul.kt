package com.netnplay.processing

interface IStateFul {
    enum class State {
        NOT_STARTED, STOPPED, STARTED, ERROR
    }
    val state: State
}