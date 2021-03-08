package com.inkostilation.pong.engine

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseMessageCommand
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException

class NullEngine<M>: IEngine<M> {
    override fun act(delta: Float) {}


    @Throws(IOException::class)
    override fun quit(marker: M) {
    }
}