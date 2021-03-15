package com.inkostilation.pong.desktop

import com.badlogic.gdx.Game
import com.inkostilation.pong.client.NetworkConnector
import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.request.CounterCommand
import com.inkostilation.pong.commands.request.InitCommand
import com.inkostilation.pong.commands.request.RequestMessageCommand
import com.inkostilation.pong.server.engine.AbstractEngine
import com.inkostilation.pong.server.engine.IEngine

class PongMain : Game() {

    private lateinit var networkConnector: NetworkConnector<Game>

    override fun create() {
        networkConnector = NetworkConnector(this, "localhost", 8080)
        networkConnector.start()
        val command = InitCommand()
        networkConnector.send(command)

        val counter = CounterCommand()
        while (true) {
            Thread.sleep(500)
            networkConnector.send(counter)
        }
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
        networkConnector.stop()
    }
}