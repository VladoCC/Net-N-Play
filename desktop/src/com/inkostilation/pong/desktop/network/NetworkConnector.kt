package com.inkostilation.pong.desktop.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.QuitCommand
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
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

class NetworkConnector<I>(private val host: String, private val port: Int): IConnector<I> {
    private lateinit var channel: SocketChannel
    private val serializer = Serializer()
    private val commandQueue = Collections.synchronizedList(ArrayList<AbstractRequestCommand<*, *>>())
    private val networkListener = NetworkListener()

    private lateinit var sendJob: Job
    private lateinit var receiveJob: Job

    override var state: IStateFul.State = IStateFul.State.NOT_STARTED
        private set

    fun act() {
        try {
            if (state == IStateFul.State.STARTED) {
                communicate()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoEngineException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    override fun start() {
        channel = SocketChannel.open(InetSocketAddress(host, port))
        state = IStateFul.State.STARTED

        sendJob = GlobalScope.launch {
            while (true) {
                if (commandQueue.size > 0) {
                    withContext(Dispatchers.IO) {
                        sendQueuedCommmands()
                    }
                    commandQueue.clear()
                }
            }
        }
        receiveJob = GlobalScope.launch {
            while (true) {
                val commands = withContext(Dispatchers.IO) {
                    receive()
                }
                for (command in commands) {
                    receiveCommand(command, null)
                }
            }
        }
    }


    @Throws(IOException::class, NoEngineException::class)
    private fun communicate() {


    }

    @Throws(IOException::class)
    override fun receive(): List<AbstractResponseCommand<I>> {
        val objects = networkListener.listen(channel!!)
        return objects.map { o ->
                    val command = serializer.deserialize(o) as AbstractResponseCommand<I>
                    command.notifier = ClientNotifier.Companion.getInstance()
                    command
                }
    }

    @Throws(IOException::class)
    override fun stop() {
        commandQueue.add(QuitCommand())
        sendQueuedCommmands()
        sendJob.cancel()
        channel.close()
    }

    override fun send(command: AbstractRequestCommand<IEngine<*>, *>) {
        commandQueue.add(command)
    }

    @Throws(IOException::class)
    private fun sendQueuedCommmands() {
        val msg = StringBuilder()
        var commands = arrayOfNulls<AbstractRequestCommand<*, *>?>(commandQueue.size)
        commands = commandQueue.toArray(commands)
        for (command in commands) {
            msg.append(serializer.serialize(command))
        }
        channel!!.write(ByteBuffer.wrap(msg.toString().toByteArray()))
    }
}