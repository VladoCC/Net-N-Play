package com.netnplay.server.network

import com.netnplay.server.engine.AbstractEngine

class Redirect {
    var direction: Class<out AbstractEngine>? = null
        private set
    var directed = false
        private set

    fun redirect(engine: Class<out AbstractEngine>) {
        direction = engine
        directed = true
    }

    fun reset() {
        direction = null
        directed = false
    }
}