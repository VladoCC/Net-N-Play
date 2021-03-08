package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.engine.IEngine
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

class NetworkProcessor(val host: String, val port: Int) : AbstractProcessor<SocketChannel>() {

    private var selector = Selector.open()
    private val serverSocket= ServerSocketChannel.open()

    private val serializer = Serializer()
    private val networkListener = NetworkListener()

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

    override fun process(): List<SocketChannel> {
        if (state == State.STARTED) {
            try {
                selector!!.select(10)
                val selectedKeys = selector!!.selectedKeys()
                selectedKeys.filter { it.isAcceptable }
                        .forEach { register() }
                return selectedKeys.filter { it.isReadable }.map { it.channel() as SocketChannel }
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
    override fun receive(marker: SocketChannel): List<AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>> {
        val set: MutableSet<Class<*>> = HashSet()
        return (serializer.deserialize(parseObjects(marker))
                as List<AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>>)
                .filter { c ->
                    set.add(c::class.java)
                }
    }

    @Throws(IOException::class)
    private fun parseObjects(channel: SocketChannel): List<String> {
        return networkListener.listen(channel)
    }

    override fun send(commands: List<AbstractResponseCommand<*>>, channel: SocketChannel) {
        if (channel.isConnected) {
            try {
                val data = ByteBuffer.wrap(serializer.serialize(commands).toByteArray())
                channel.write(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}