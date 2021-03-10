package com.inkostilation.pong.client

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.processing.IStateFul
import com.inkostilation.pong.processing.NetworkListener
import com.inkostilation.pong.processing.Serializer
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws
import kotlinx.coroutines.*

class NetworkConnector<I>(private val receiver: I, private val host: String, private val port: Int): IConnector<I> {
    private lateinit var channel: SocketChannel
    private val serializer = Serializer()
    private val commandQueue = Collections.synchronizedList(ArrayList<AbstractRequestCommand<*, *>>())
    private val networkListener = NetworkListener()

    private lateinit var senderThread: SenderThread
    private lateinit var receiverThread: ReceiverThread

    override var state: IStateFul.State = IStateFul.State.NOT_STARTED
        private set

    override fun start() {
        try {
            channel = SocketChannel.open(InetSocketAddress(host, port))
            state = IStateFul.State.STARTED

            senderThread = SenderThread()
            senderThread.start()

            receiverThread = ReceiverThread()
            receiverThread.start()
        } catch (e: Exception) {
            e.printStackTrace()
            state = IStateFul.State.ERROR
        }
    }

    @Throws(IOException::class)
    override fun receive(): List<AbstractResponseCommand<I>> {
        return (serializer.deserialize(networkListener.listen(channel)) as List<AbstractResponseCommand<I>>)
    }

    @Throws(IOException::class)
    override fun stop() {
        commandQueue.clear()
        commandQueue.add(QuitCommand())
        sendQueuedCommmands()

        senderThread.finish()
        receiverThread.finish()

        while (!senderThread.finished || !receiverThread.finished)
        channel.close()

        state = IStateFul.State.STOPPED
    }

    override fun send(command: AbstractRequestCommand<IEngine<*>, *>) {
        commandQueue.add(command)
    }

    @Throws(IOException::class)
    private fun sendQueuedCommmands() {
        // todo check for concurent modifications
        //val commands= commandQueue.toTypedArray()
        val commands = commandQueue as List<AbstractRequestCommand<*, *>>
        channel.write(ByteBuffer.wrap(serializer.serialize(commands).toByteArray()))
    }

    private inner class SenderThread: Thread() {
        var work = true
            private set
        var finished = false
            private set

        override fun run() {
            while (work) {
                if (commandQueue.size > 0) {
                    sendQueuedCommmands()

                    commandQueue.clear()
                }
            }

            finished = true
        }

        fun finish() {
            work = false
        }
    }

    private inner class ReceiverThread: Thread() {
        var work = true
            private set
        var finished = false
            private set

        init {
            priority = 1
        }

        override fun run() {
            while (work) {
                receive().forEach { it.execute(receiver) }
            }
            finished = true
        }

        fun finish() {
            work = false
        }
    }
}