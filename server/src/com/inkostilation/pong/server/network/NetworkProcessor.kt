package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.IEngine
import com.inkostilation.pong.processing.IStateFul
import com.inkostilation.pong.processing.NetworkListener
import com.inkostilation.pong.processing.Serializer
import com.inkostilation.pong.processing.IStateFul.State
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws
import kotlin.reflect.KClass

class NetworkProcessor(val host: String, val port: Int) : IProcessor<SocketChannel> {

    private var selector = Selector.open()
    private val serverSocket= ServerSocketChannel.open()

    private val serializer = Serializer()
    private val networkListener = NetworkListener()
    override lateinit var router: ICommandRouter<SocketChannel>

    private val commandQueue: MutableMap<SocketChannel, MutableList<AbstractResponseCommand<*>>> = HashMap()

    override var state = State.NOT_STARTED
        private set

    override fun start(){
        try {
            serverSocket.bind(InetSocketAddress(host, port))
            serverSocket.configureBlocking(false)
            serverSocket.register(selector, SelectionKey.OP_ACCEPT)
            state = State.STARTED
        } catch (e: Exception) {
            e.printStackTrace()
            state = State.ERROR
        }
    }

    override fun stop() {
        state = State.STOPPED
    }

    override fun processConnections() {
        if (state == State.STARTED) {
            try {
                selector!!.select(10)
                val selectedKeys = selector!!.selectedKeys()
                val iter = selectedKeys.iterator()
                while (iter.hasNext()) {
                    val key = iter.next()
                    if (key.isAcceptable) {
                        register()
                    }
                    if (key.isReadable) {
                        receiveMessage(key)
                    }
                    iter.remove()
                }
                if (commandQueue.size > 0) {
                    sendAll()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun register() {
        val client = serverSocket!!.accept()
        client.configureBlocking(false)
        client.register(selector, SelectionKey.OP_READ)
    }

    @Throws(IOException::class)
    private fun receiveMessage(key: SelectionKey) {
        val objects = parseObjects(key.channel() as SocketChannel)
        val set: MutableSet<KClass<*>> = HashSet()
        val commands: List<AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>>
                = objects.map { o: String ->
                    val command = serializer.deserialize(o) as AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>
                            ?: return@map null
                    command.marker = key.channel() as SocketChannel
                    command
                }
                .filterNotNull()
                .filter { c ->
                    set.add(c::class) }
        for (command in commands) {
            try {
                router.route(command, this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun parseObjects(channel: SocketChannel): List<String> {
        return networkListener.listen(channel)
    }

    override fun sendMessage(command: AbstractResponseCommand<*>, channel: SocketChannel) {
        if (commandQueue.containsKey(channel)) {
            commandQueue[channel]!!.add(command)
        } else {
            val commands: MutableList<AbstractResponseCommand<*>> = ArrayList()
            commands.add(command)
            commandQueue[channel] = commands
        }
    }

    fun hasQueuedMessages(channel: SocketChannel?): Boolean {
        return commandQueue.containsKey(channel) && commandQueue[channel]!!.size > 0
    }

    private fun sendAll() {
        for ((key, value) in commandQueue) {
            if (key!!.isConnected) {
                val message = StringBuilder()
                for (command in value) {
                    message.append(serializer.serialize(command))
                }
                try {
                    key.write(ByteBuffer.wrap(message.toString().toByteArray()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        commandQueue.clear()
    }
}