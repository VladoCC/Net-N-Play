package com.netnplay.server.network

import com.netnplay.server.engine.AbstractEngine

/**
 * Special class, designed to be used as a directive,
 * that client can call to in case they want to move from one
 * engine to another.
 *
 * This directive can be called from [com.netnplay.server.engine.IEngine],
 * which mean that client can call to it from [com.netnplay.commands.AbstractRequestCommand].
 *
 * See [ICommandRouter] for more information about how this
 * directives are processed and ways to limit access rights to
 * different engines.
 */
class Redirect {
    var direction: Class<out AbstractEngine>? = null
        private set
    var directed = false
        private set

    /**
     * Sets the [engine] as an engine that the client wants to be redirected to.
     */
    fun redirect(engine: Class<out AbstractEngine>) {
        direction = engine
        directed = true
    }

    /**
     * Clears all the information about this redirect and makes
     * it ready to be used again later, which allows to use one redirect
     * for many clients at the same time and not wasting resources
     * for destructing it and constructing a new one.
     */
    fun reset() {
        direction = null
        directed = false
    }
}