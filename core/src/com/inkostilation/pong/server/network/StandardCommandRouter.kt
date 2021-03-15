package com.inkostilation.pong.server.network

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseErrorCommand
import com.inkostilation.pong.server.engine.IEngine
import com.inkostilation.pong.exceptions.NoEngineException
import com.inkostilation.pong.server.engine.AbstractEngine
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

class StandardCommandRouter(engines: List<AbstractEngine> = ArrayList(), val rerouteRule: (UUID, Class<out AbstractEngine>) -> Boolean = { _,_ -> true}) : AbstractCommandRouter() {
    private lateinit var engines: Map<Class<out AbstractEngine>, AbstractEngine>
    private lateinit var markedEngines: MutableMap<UUID, Class<out AbstractEngine>>
    private val redirect = Redirect()

    init {
        start(engines)
    }

    override fun start(engines: List<AbstractEngine>) {
        this.engines = engines.map {
                    e -> e::class.java to e
                }
                .toMap()
        markedEngines = mutableMapOf()
    }

    override fun canReroute(marker: UUID, direction: Class<out AbstractEngine>) = rerouteRule(marker, direction)

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

    override fun reroute(marker: UUID, engine: Class<out AbstractEngine>): AbstractEngine {
        markedEngines[marker] = engine
        return engines[markedEngines[marker]]!!
    }

    override fun stop() {
    }
}