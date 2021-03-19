package com.netnplay.server.network

import com.google.common.collect.HashBiMap
import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.processing.IStateFul.State
import com.netnplay.processing.NetworkListener
import com.netnplay.processing.Serializer
import com.netnplay.server.engine.AbstractEngine
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.*

class NetworkProcessor(val host: String, val port: Int) : AbstractProcessor() {

    private var selector = Selector.open()
    private val serverSocket= ServerSocketChannel.open()

    private val serializer = Serializer()
    private val networkListener = NetworkListener()

    private val socketMap = HashBiMap.create<UUID, SocketChannel>()

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
                val selectedKeys = selector.selectedKeys()
                val iter = selectedKeys.iterator()

                val keyList = mutableListOf<SelectionKey>()

                while (iter.hasNext()) {
                    val key = iter.next()
                    if (key.isAcceptable) {
                        register()
                    }
                    if (key.isReadable) {
                        keyList.add(key)
                    }
                    iter.remove()
                }
                return processKeys(keyList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return emptyList()
    }

    private fun processKeys(keys: List<SelectionKey>): List<UUID> {
        return keys.map {
            val channel = it.channel() as SocketChannel
            var uuid: UUID
            if (socketMap.containsValue(channel)) {
                uuid = socketMap.inverse()[channel]?: UUID.randomUUID()
            } else {
                uuid = UUID.randomUUID()
                while (socketMap.containsKey(uuid)) {
                    uuid = UUID.randomUUID()
                }
            }
            socketMap[uuid] = channel
            return@map uuid
        }
    }

    @Throws(IOException::class)
    private fun register() {
        val client = serverSocket!!.accept()
        client.configureBlocking(false)
        client.register(selector, SelectionKey.OP_READ)
    }

    @Throws(IOException::class)
    override fun receive(marker: UUID): List<AbstractRequestCommand<AbstractEngine>> {
        val set: MutableSet<Class<*>> = HashSet()
        return (serializer.deserialize(socketMap[marker]?.let { parseObjects(it) })
                as List<AbstractRequestCommand<AbstractEngine>>)
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