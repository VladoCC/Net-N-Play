package com.inkostilation.pong.engine

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException

class NullEngine<M> private constructor() : IEngine<M> {
    override fun act(delta: Float) {}
    @Throws(IOException::class, NoEngineException::class)
    fun receiveCommand(command: AbstractResponseCommand?, mark: Any?) {
    }

    @Throws(IOException::class, NoEngineException::class)
    fun sendCommand(vararg request: AbstractRequestCommand<IEngine<M>?, M>?): AbstractResponseCommand {
    }

    @Throws(IOException::class)
    override fun quit(marker: M) {
    }

    companion object {
        val INSTANCE: NullEngine<*> = NullEngine<Any?>()
    }
}