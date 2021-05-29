package com.netnplay.processing

import com.netnplay.exceptions.EmptyParcelException
import com.netnplay.exceptions.ParsingNotFinishedException
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

/**
 * Class designed to receive data from opened [SocketChannel] and turning it
 * from raw bytes to [List] of json objects.
 */
class NetworkListener(bufferSize: Int = 1024) {
    private val parser = MessageParser()
    private val buffer = ByteBuffer.allocate(bufferSize)

    /**
     * Main method of this class.
     * Gets [channel], listens to it and tries to parse objects
     * from received data if possible.
     */
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
            if (parser.isEmpty || parser.hasObjects) {
                try {
                    objects = parser.getAndClear()
                } catch (e: ParsingNotFinishedException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return objects
    }
}