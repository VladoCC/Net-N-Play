package com.inkostilation.pong.server

import com.inkostilation.pong.server.engine.MainEngine
import com.inkostilation.pong.server.engine.NullEngine
import com.inkostilation.pong.server.engine.StarterEngine
import java.io.IOException
import java.nio.channels.SocketChannel
import kotlin.jvm.Throws

object ServerLauncher {
    @Throws(IOException::class)
    @JvmStatic
    fun main(arg: Array<String>) {
        ServerApplication.Builder()
                .addEngines(arrayOf(StarterEngine(), MainEngine()))
                .build(StarterEngine::class.java)
                .start()
    }
}