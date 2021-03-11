package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

class StandardCommandRouter(engines: List<IEngine> = ArrayList()) : AbstractCommandRouter() {
    private lateinit var engines: Map<Class<out IEngine>, IEngine>
    private lateinit var markedEngines: MutableMap<UUID, Class<out IEngine>>
    private val redirect = Redirect()

    init {
        start(engines)
    }

    override fun start(engines: List<IEngine>) {
        this.engines = engines.map {
                    e -> e::class.java to e
                }
                .toMap()
        markedEngines = mutableMapOf()
    }

    @Throws(IOException::class, NoEngineException::class)
    override fun route(command: AbstractRequestCommand<IEngine>, marker: UUID): List<AbstractResponseCommand<*>> {
        val type = command.getEngineType() as Class<out IEngine>
        return if (markedEngines[marker] == type) {
            val engine = engines[type]
            if (engine != null) {
                val response = engine?.process(command)
                        ?.toList()

                val redirect = engine.getRedirect()
                if (redirect.directed) {
                    reroute(marker, redirect.direction!!)
                }

                response
            }
            emptyList()
        } else {
            listOf(ResponseErrorCommand("You don't have permission to interact with engine of class $type."))
        }
    }

    override fun onQuitCommand(marker: UUID, processor: IProcessor) {
        try {
            getEngine(marker)!!.quit(marker)
            processor.closeConnection(marker)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun getEngine(marker: UUID): IEngine? {
        return if (markedEngines.containsKey(marker)) {
            engines[markedEngines[marker]]
        } else {
            null
        }
    }

    override fun reroute(marker: UUID, engine: Class<out IEngine>): Boolean {
        val last = markedEngines[marker]
        if (last != null) {
            engines[last]?.quit(marker)
        }
        markedEngines[marker] = engine
        engines[engine]?.enter(marker)
        return true
    }

    override fun stop() {
    }
}