package com.inkostilation.pong.desktop

import com.badlogic.gdx.Game
import com.inkostilation.pong.desktop.display.LobbyScreen
import com.inkostilation.pong.desktop.network.ServerThread

class PongMain : Game() {
    override fun create() {
        setScreen(LobbyScreen())
        ServerThread.Companion.getInstance().start()
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
        ServerThread.Companion.getInstance().stopServer()
        while (!ServerThread.Companion.getInstance().isFinished()) {
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}