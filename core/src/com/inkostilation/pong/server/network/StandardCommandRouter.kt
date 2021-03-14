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

    override fun canReroute(marker: UUID, direction: Class<out IEngine>) = engines[direction] != null

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

    override fun reroute(marker: UUID, engine: Class<out IEngine>): IEngine {
        markedEngines[marker] = engine
        return engines[markedEngines[marker]]!!
    }

    override fun stop() {
    }
}