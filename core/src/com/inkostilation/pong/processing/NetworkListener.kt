package com.inkostilation.pong.processing

import com.inkostilation.pong.exceptions.EmptyParcelException
import com.inkostilation.pong.exceptions.ParsingNotFinishedException
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws

class NetworkListener(bufferSize: Int = 1024) {
    private val parser = MessageParser()
    private val buffer = ByteBuffer.allocate(bufferSize)

    @Throws(IOException::class)
    fun listen(channel: SocketChannel): List<String> {
        var objects: List<String> = ArrayList()
        /** message parsing  */
        parser.newMessage()
        while (true) {
            try {
                channel.read(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
                channel.close()
            }
            try {
                parser.addParcel(buffer)
            } catch (e: EmptyParcelException) {
                e.printStackTrace()
                break
            }
            (buffer as Buffer).clear()
            if (parser.isEmpty || parser.hasObjects()) {
                try {
                    objects = parser.andClearObjects
                } catch (e: ParsingNotFinishedException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return objects
    }
}