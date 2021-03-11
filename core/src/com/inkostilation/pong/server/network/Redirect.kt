package com.inkostilation.pong.server.network

import com.inkostilation.pong.server.engine.IEngine

class Redirect {
    var direction: Class<out IEngine>? = null
    var directed = false

    fun redirect(engine: Class<out IEngine>) {
        direction = engine
        directed = true
    }

    fun reset() {
        direction = null
        directed = false
    }
}