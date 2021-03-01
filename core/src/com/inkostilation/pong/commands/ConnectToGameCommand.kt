package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IPongEngine
import java.io.IOException

class ConnectToGameCommand<M> : AbstractPongCommand<M>() {
    @Throws(IOException::class)
    override fun execute(engine: IPongEngine<*>) {
        getEngine().startNewGame(marker)
    }
}