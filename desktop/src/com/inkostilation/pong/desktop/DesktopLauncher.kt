package com.inkostilation.pong.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.commands.response.ResponseMessageCommand
import com.inkostilation.pong.client.PongMain
import com.inkostilation.pong.processing.Serializer

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration()
        val serializer = Serializer()
        val str = serializer.serialize(ResponseErrorCommand("test"))
        val str2 = serializer.serialize(ResponseMessageCommand("test"))
        config.width = 1280
        config.height = 720
        LwjglApplication(PongMain(), config)
    }
}