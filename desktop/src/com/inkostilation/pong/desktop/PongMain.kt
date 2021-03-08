package com.inkostilation.pong.desktop

import com.badlogic.gdx.Game
import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.request.RequestMessageCommand
import com.inkostilation.pong.desktop.network.NetworkConnector
import com.inkostilation.pong.engine.IEngine

class PongMain : Game() {

    private lateinit var networkConnector: NetworkConnector<Game>

    override fun create() {
        networkConnector = NetworkConnector(this, "localhost", 8080)
        networkConnector.start()
        val command = RequestMessageCommand<Void>("ping")
        networkConnector.send(command as AbstractRequestCommand<IEngine<*>, *>)
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
        networkConnector.stop()
    }
}