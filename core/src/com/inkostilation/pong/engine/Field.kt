package com.inkostilation.pong.engine

import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.commands.response.ResponseFieldCommand
import com.inkostilation.pong.engine.geometry.Rectangle

class Field : Rectangle(0f, 0f, 700f, 500f), IUpdatable, ICommandSender {
    val paddle1: Paddle
    val paddle2: Paddle
    val ball: Ball
    var isStarted = false

    val isBallInBounds: Boolean
        get() = ball.isInBounds(this)

    override fun update(delta: Float) {
        paddle1.update(delta)
        paddle2.update(delta)
        if (isStarted) {
            ball.update(delta)
        }
        if (!paddle1.isInBounds(this)) {
            paddle1.constrain(this)
        }
        if (!paddle2.isInBounds(this)) {
            paddle2.constrain(this)
        }
        if (!ball.isInBounds(this)) {
            onBallOutOfBounds()
        }
        if (ball.isColliding(paddle1)) {
            ball.applyCollision(paddle1)
        }
        if (ball.isColliding(paddle2)) {
            ball.applyCollision(paddle2)
        }
    }

    private fun onBallOutOfBounds() {
        if (ball.y - ball.radius <= y
                || ball.y + ball.radius >= y + height) {
            if (ball.x - ball.radius >= x && ball.x + ball.radius <= x + width) {
                ball.constrain(this)
            }
        }
    }

    fun reset() {
        paddle1.reset()
        paddle2.reset()
        ball.resetPosition()
        ball.resetVelocity(if (ball.x - ball.radius < x) Ball.Direction.LEFT else Ball.Direction.RIGHT)
        isStarted = false
    }

    fun setControlled(playerRole: PlayerRole?, state: Boolean) {
        when (playerRole) {
            PlayerRole.FIRST -> {
                paddle1.isControlled = state
            }
            PlayerRole.SECOND -> {
                paddle2.isControlled = state
            }
        }
    }

    fun isControlled(playerRole: PlayerRole?): Boolean {
        when (playerRole) {
            PlayerRole.FIRST -> {
                return paddle1.isControlled
            }
            PlayerRole.SECOND -> {
                return paddle2.isControlled
            }
        }
        return false
    }

    fun getPaddle(playerRole: PlayerRole?): Paddle? {
        when (playerRole) {
            PlayerRole.FIRST -> {
                return paddle1
            }
            PlayerRole.SECOND -> {
                return paddle2
            }
        }
        return null
    }

    override fun hasCommands(): Boolean {
        return true
    }

    override fun getCommands(pool: MutableList<AbstractResponseCommand?>) {
        pool.add(ResponseFieldCommand(this))
    }

    init {
        paddle1 = Paddle(20, 210)
        paddle2 = Paddle(660, 210)
        paddle1.playerRole = PlayerRole.FIRST
        paddle2.playerRole = PlayerRole.SECOND
        ball = Ball(350, 250)
    }
}