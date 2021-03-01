package com.inkostilation.pong.commands.request

import com.inkostilation.pong.commands.AbstractPongCommand
import com.inkostilation.pong.engine.IPongEngine
import java.io.IOException

class RequestPlayerRoleCommand<M> : AbstractPongCommand<M>() {
    @Throws(IOException::class)
    override fun execute(engine: IPongEngine<*>) {
        getEngine().sendPlayerRole(marker)
    }
}