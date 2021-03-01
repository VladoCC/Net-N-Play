package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IEngine
import java.io.IOException
import java.nio.channels.SocketChannel

class QuitCommand : AbstractRequestCommand<IEngine<SocketChannel?>?, SocketChannel?>() {
    override fun execute(engine: IEngine<SocketChannel?>?): Array<out AbstractResponseCommand>? {
        try {
            getEngine().quit(marker)
            marker.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}