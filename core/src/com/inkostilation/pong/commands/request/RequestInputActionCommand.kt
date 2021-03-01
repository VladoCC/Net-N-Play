package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractPongCommand
import com.inkostilation.pong.engine.Direction
import com.inkostilation.pong.engine.IPongEngine
import java.io.IOException

class RequestInputActionCommand<M>(private val direction: Direction) : AbstractPongCommand<M>() {
    @Throws(IOException::class)
    override fun execute(engine: IPongEngine<M>) {
        getEngine().applyInput(direction, marker)
    }

}