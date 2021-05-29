package com.netnplay.client

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.commands.QuitCommand
import com.netnplay.processing.IStateFul
import com.netnplay.processing.NetworkListener
import com.netnplay.processing.Serializer
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws

/**
 * Standard realisation of [IConnector] interface,
 * built around [SocketChannel] and designed to be used for network intercations.
 */
class NetworkConnector<I>(private val executionContext: I, private val host: String, private val port: Int): IConnector<I> {
    private lateinit var channel: SocketChannel
    private val serializer = Serializer()
    private val commandQueue = Collections.synchronizedList(ArrayList<AbstractRequestCommand<*>>())
    private val networkListener = NetworkListener()

    private lateinit var senderThread: SenderThread
    private lateinit var receiverThread: ReceiverThread

    private var state = IStateFul.State.NOT_STARTED

    override fun getState() = state

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
        sendQueuedCommands()

        senderThread.finish()
        receiverThread.finish()

        while (!senderThread.finished || !receiverThread.finished)
        channel.close()

        state = IStateFul.State.STOPPED
    }

    override fun send(command: AbstractRequestCommand<*>) {
        commandQueue.add(command)
    }

    /**
     * NetworkConnector sends commands in batches.
     */
    @Throws(IOException::class)
    private fun sendQueuedCommands() {
        // creating new list to make sure that there is no concurrent modification problems
        val commands = commandQueue.toList() as List<AbstractRequestCommand<*>>
        channel.write(ByteBuffer.wrap(serializer.serialize(commands).toByteArray()))
    }

    /**
     * Thread responsible for sending batched commands to the server
     */
    private inner class SenderThread: Thread() {
        var work = true
            private set
        var finished = false
            private set

        override fun run() {
            while (work) {
                if (commandQueue.size > 0) {
                    sendQueuedCommands()

                    commandQueue.clear()
                }
            }

            finished = true
        }

        fun finish() {
            work = false
        }
    }

    /**
     * Thread responsible for receiving response commands from the server.
     * It receives batch of commands and executes them.
     * This thread is blocking.
     */
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