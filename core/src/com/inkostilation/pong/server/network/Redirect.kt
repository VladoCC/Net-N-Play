package com.inkostilation.pong.server.network

import com.inkostilation.pong.server.engine.IEngine

class Redirect {
    var direction: Class<out IEngine>? = null
        private set
    var directed = false
        private set

    fun redirect(engine: Class<out IEngine>) {
        direction = engine
        directed = true
    }

    fun reset() {
        direction = null
        directed = false
    }
}