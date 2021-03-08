package com.inkostilation.pong.server

import com.inkostilation.pong.engine.NullEngine
import com.inkostilation.pong.server.application.ServerApplication
import java.io.IOException
import kotlin.jvm.Throws

object ServerLauncher {
    @Throws(IOException::class)
    @JvmStatic
    fun main(arg: Array<String>) {
        ServerApplication.Builder().addEngine(NullEngine()).build().start()
    }
}