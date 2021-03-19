package com.netnplay.client

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.commands.QuitCommand
import com.netnplay.processing.IStateFul
import com.netnplay.processing.NetworkListener
import com.netnplay.processing.Serializer
import com.netnplay.server.engine.AbstractEngine
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws

class NetworkConnector<I>(private val executionContext: I, private val host: String, private val port: Int): IConnector<I> {
    private lateinit var channel: SocketChannel
    private val serializer = Serializer()
    private val commandQueue = Collections.synchronizedList(ArrayList<AbstractRequestCommand<*>>())
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

    override fun send(command: AbstractRequestCommand<*>) {
        commandQueue.add(command)
    }

    @Throws(IOException::class)
    private fun sendQueuedCommmands() {
        // creating new list to make sure that there is no concurrent modification problems
        val commands = listOf(commandQueue) as List<AbstractRequestCommand<AbstractEngine>>
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

        override fun run() {
            while (work) {
                receive().forEach { it.execute(executionContext) }
            }
            finished = true
        }

        fun finish() {
            work = false
        }
    }
}