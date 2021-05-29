package com.netnplay.processing

/**
 * Interface for representing state of any object that has lifecycle.
 */
interface IStateFul {
    /**
     * Enumerated states for describing lifecycle of the object that can be
     * started, paused, stopped or downed by the error
     */
    enum class State {
        NOT_STARTED, STOPPED, STARTED, ERROR
    }
    fun getState(): State
}