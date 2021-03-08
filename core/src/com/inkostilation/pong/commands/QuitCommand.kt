package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IEngine
import java.io.IOException
import java.nio.channels.SocketChannel
import kotlin.reflect.KClass

class QuitCommand() : AbstractRequestCommand<IEngine<Any>, Any>() {
    override fun execute(engine: IEngine<Any>): Array<AbstractResponseCommand<*>> {
        return emptyArray()
    }

    override fun getEngineType() = IEngine::class.java
}