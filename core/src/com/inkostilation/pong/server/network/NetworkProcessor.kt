package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.processing.NetworkListener
import com.inkostilation.pong.processing.IStateFul.State
import com.inkostilation.pong.processing.Serializer
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*
import kotlin.jvm.Throws

class NetworkProcessor(val host: String, val port: Int) : AbstractProcessor() {

    private var selector = Selector.open()
    private val serverSocket= ServerSocketChannel.open()

    private val serializer = Serializer()
    private val networkListener = NetworkListener()

    private val socketMap = mutableMapOf<UUID, SocketChannel>()

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

    override fun closeConnection(marker: UUID) {
        val socketChannel = socketMap[marker]
        socketChannel?.close()
        socketMap.remove(marker)
    }

    override fun stop() {
        serverSocket.close()
        state = State.STOPPED
    }

    override fun process(): List<UUID> {
        if (state == State.STARTED) {
            try {
                selector!!.select(10)
                val selectedKeys = selector!!.selectedKeys()
                selectedKeys.filter { it.isAcceptable }
                        .forEach { register() }
                return selectedKeys.filter { it.isReadable }.map {
                    var uuid = UUID.randomUUID()
                    while (socketMap.containsKey(uuid)) {
                        uuid = UUID.randomUUID()
                    }
                    socketMap[uuid] = it.channel() as SocketChannel
                    return@map uuid
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return emptyList()
    }

    @Throws(IOException::class)
    private fun register() {
        val client = serverSocket!!.accept()
        client.configureBlocking(false)
        client.register(selector, SelectionKey.OP_READ)
    }

    @Throws(IOException::class)
    override fun receive(marker: UUID): List<AbstractRequestCommand<IEngine>> {
        val set: MutableSet<Class<*>> = HashSet()
        return (serializer.deserialize(socketMap[marker]?.let { parseObjects(it) })
                as List<AbstractRequestCommand<IEngine>>)
                .filter { c ->
                    set.add(c::class.java)
                }
    }

    @Throws(IOException::class)
    private fun parseObjects(channel: SocketChannel): List<String> {
        return networkListener.listen(channel)
    }

    override fun send(commands: List<AbstractResponseCommand<*>>, marker: UUID) {
        val channel = socketMap[marker]
        if (channel != null && channel.isConnected) {
            try {
                val data = ByteBuffer.wrap(serializer.serialize(commands).toByteArray())
                channel.write(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}