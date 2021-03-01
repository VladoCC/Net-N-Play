package com.inkostilation.pong.engine

import com.inkostilation.pong.engine.geometry.Rectangle

class Paddle(private val defaultX: Float, private val defaultY: Float) : Rectangle(defaultX, defaultY, 20f, 80f), IUpdatable {
    private var yVel = 0f
    var playerRole: PlayerRole? = null
    var isControlled = false
    private var accelerationDirection: Direction? = null
    fun reset() {
        x = defaultX
        y = defaultY
        yVel = 0f
        setDefaultState()
    }

    private fun setDefaultState() {
        yVel = 0f
        accelerationDirection = Direction.IDLE
        isControlled = false
    }

    fun setAccelerationDirection(accelerationDirection: Direction?) {
        this.accelerationDirection = accelerationDirection
    }

    override fun update(delta: Float) {
        val dir = accelerationDirection!!.value
        if (dir != 0) yVel += ACCELERATION * dir * delta else yVel *= 1 - FRICTION * delta
        if (yVel >= MAX_VELOCITY) yVel = MAX_VELOCITY else if (yVel <= -MAX_VELOCITY) yVel = -MAX_VELOCITY
        y = y + yVel * delta
    }

    fun constrain(rectangle: Rectangle) {
        if (y < 0) {
            y = 0f
        } else if (y > rectangle.height - height) {
            y = rectangle.height - height
        }
    }

    companion object {
        private const val FRICTION = 0.07f
        private const val ACCELERATION = 40f
        private const val MAX_VELOCITY = 90f
    }

    init {
        setDefaultState()
    }
}