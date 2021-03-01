package com.inkostilation.pong.desktop.network

import java.io.IOException

class ServerThread : Thread() {
    private var active = false
    var isFinished = false
        private set

    @Synchronized
    override fun start() {
        active = true
        super.start()
    }

    override fun run() {
        while (active) {
            Network.getEngine().act(0f)
        }
        // last messages to disconnect and etc.
        try {
            Network.getEngine().quit(null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        isFinished = true
    }

    fun stopServer() {
        active = false
    }

    companion object {
        var instance: ServerThread? = null
            get() {
                if (field == null) {
                    field = ServerThread()
                }
                return field
            }
            private set
    }
}