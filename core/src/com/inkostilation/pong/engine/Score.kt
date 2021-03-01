package com.inkostilation.pong.engine

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseScoreCommand
import java.util.*
import java.util.stream.Collectors

class Score : ICommandSender {
    val maxScoreValue = 5
    private val points: MutableMap<PlayerRole, Int> = HashMap()
    private var changed = false
    fun getPlayerScore(role: PlayerRole?): Int {
        return points.get(role)
    }

    fun addPlayerScore(role: PlayerRole, add: Int) {
        val `val` = points[role]!!
        points.replace(role, `val` + add)
        if (add != 0) {
            changed = true
        }
    }

    val maxValueCount: Long
        get() = points.values
                .stream()
                .filter { o: Int -> o >= maxScoreValue }
                .count()

    val maxedPlayers: List<PlayerRole?>
        get() = points.entries
                .stream()
                .filter { o: Map.Entry<PlayerRole, Int> -> o.value >= maxScoreValue }
                .map { o: Map.Entry<PlayerRole, Int> -> o.key }
                .collect(Collectors.toList())

    override fun hasCommands(): Boolean {
        return changed
    }

    override fun getCommands(pool: MutableList<AbstractResponseCommand?>) {
        if (changed) {
            pool.add(ResponseScoreCommand(this))
            changed = false
        }
    }

    init {
        points[PlayerRole.FIRST] = 0
        points[PlayerRole.SECOND] = 0
    }
}