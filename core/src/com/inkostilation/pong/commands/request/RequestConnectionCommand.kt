package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractPongCommand
import com.inkostilation.pong.engine.IPongEngine
import java.io.IOException

class RequestConnectionCommand<M>(private val index: Int) : AbstractPongCommand<M>() {
    @Throws(IOException::class)
    protected override fun execute(engine: IPongEngine<M>) {
        engine.connectToGame(marker, index)
    }

}