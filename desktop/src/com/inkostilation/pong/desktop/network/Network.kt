package com.inkostilation.pong.desktop.network

import com.inkostilation.pong.engine.IEngine

object Network {
    var engine: IEngine<*>? = null
        get() {
            if (field == null) {
                field = NetworkConnector()
            }
            return field
        }
        private set
}