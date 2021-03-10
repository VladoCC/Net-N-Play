package com.inkostilation.pong.server

import com.inkostilation.pong.server.engine.NullEngine
import java.io.IOException
import java.nio.channels.SocketChannel
import kotlin.jvm.Throws

object ServerLauncher {
    @Throws(IOException::class)
    @JvmStatic
    fun main(arg: Array<String>) {
        ServerApplication.Builder().addEngine(NullEngine()).build(NullEngine::class.java).start()
    }
}