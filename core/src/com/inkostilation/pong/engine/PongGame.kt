package com.inkostilation.pong.engine

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.ListMultimap
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseGameStateCommand
import java.util.*

class PongGame : IUpdatable, ICommandSender {
    val field: Field
    val score: Score
    private var gameState = GameState.INACTIVE
    var playersNumber: Int
        private set
    private var winner: PlayerRole? = null
    private var active = true
    private var changed = true
    private var timer = 0f
    private val roles = LinkedList<PlayerRole>()

    fun getGameState(): GameState {
        return gameState
    }

    fun setGameState(gameState: GameState) {
        if (gameState != GameState.INACTIVE) {
            activeGamesMap.remove(this.gameState, this)
            activeGamesMap.put(gameState, this)
        }
        changed = true
        this.gameState = gameState
    }

    fun addPlayer(): PlayerRole {
        ++playersNumber
        return if (roles.size > 1) roles.pop() else roles.peek()
    }

    fun removePlayer(role: PlayerRole) {
        --playersNumber
        if (role != PlayerRole.DENIED) {
            roles.push(role)
            setGameState(GameState.WAITING)
            field.reset()
        }
        if (playersNumber == 0) {
            stop()
        }
    }

    fun start() { //active = true;
        setGameState(GameState.PREPARATION)
    }

    fun stop() {
        activeGamesMap.remove(gameState, this)
        active = false
        setGameState(GameState.INACTIVE)
    }

    fun scorePoint(role: PlayerRole) {
        score.addPlayerScore(role, 1)
    }

    override fun update(delta: Float) {
        if (active) {
            if (gameState == GameState.AFTER_GOAL_CONFIRMATION) {
                timer += delta
                if (timer >= AFTER_GOAL_TIME) {
                    if (score.maxValueCount == 0L) {
                        field.reset()
                        setGameState(GameState.WAITING)
                    } else {
                        winner = score.maxedPlayers[0]
                        stop()
                    }
                }
            } else if (gameState == GameState.PREPARATION) {
                timer += delta
                if (timer >= PREP_TIME) {
                    field.isStarted = true
                    setGameState(GameState.PLAYING)
                    timer = 0f
                }
            }
            field.update(delta)
            if (!field.isBallInBounds && gameState == GameState.PLAYING) {
                scorePoint(if (field.ball.x < 0) PlayerRole.SECOND else PlayerRole.FIRST)
                setGameState(GameState.AFTER_GOAL_CONFIRMATION)
                timer = 0f
            }
        }
    }

    fun setControlled(playerRole: PlayerRole?, state: Boolean) {
        field.setControlled(playerRole, state)
    }

    fun isControlled(playerRole: PlayerRole?): Boolean {
        return field.isControlled(playerRole)
    }

    fun getPaddle(playerRole: PlayerRole?): Paddle? {
        return field.getPaddle(playerRole)
    }

    fun setPlayerRole(playerRole: PlayerRole?) {
        field.getPaddle(playerRole).playerRole = playerRole
    }

    override fun hasCommands(): Boolean {
        return changed || field.hasCommands() || score.hasCommands()
    }

    override fun getCommands(pool: MutableList<AbstractResponseCommand?>) {
        if (changed) {
            changed = false
            pool.add(ResponseGameStateCommand(gameState))
        }
        field.getCommands(pool)
        score.getCommands(pool)
    }

    companion object {
        private const val AFTER_GOAL_TIME = 3.0f
        private const val PREP_TIME = 3.0f
        val activeGamesMap: ListMultimap<GameState, PongGame> = ArrayListMultimap.create()
        fun getWaitingGame(index: Int): PongGame {
            return if (!activeGamesMap.isEmpty && !getGameCollection(GameState.WAITING).isEmpty()) {
                getGame(GameState.WAITING, index)
            } else {
                PongGame()
            }
        }

        fun getGameCollection(state: GameState?): List<PongGame> {
            return activeGamesMap[state]
        }

        fun getGame(state: GameState?, index: Int): PongGame {
            return getGameCollection(state)[index]
        }

    }

    init {
        field = Field()
        score = Score()
        playersNumber = 0
        gameState = GameState.WAITING
        activeGamesMap.put(gameState, this)
        roles.add(PlayerRole.FIRST)
        roles.add(PlayerRole.SECOND)
        roles.add(PlayerRole.DENIED)
    }
}