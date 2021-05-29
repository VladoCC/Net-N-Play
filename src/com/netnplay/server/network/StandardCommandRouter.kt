package com.netnplay.server.network

import com.netnplay.server.engine.AbstractEngine
import com.netnplay.server.engine.IEngine
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Standard implementation of [ICommandRouter], that has all of the important functionality.
 */
class StandardCommandRouter(val rerouteRule: (UUID, Class<out AbstractEngine>) -> Boolean = { _,_ -> true}) : AbstractCommandRouter() {
    private lateinit var engines: Map<Class<out AbstractEngine>, AbstractEngine>
    private lateinit var markedEngines: MutableMap<UUID, Class<out AbstractEngine>>

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