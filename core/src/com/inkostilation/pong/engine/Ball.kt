package com.inkostilation.pong.engine

import com.inkostilation.pong.engine.geometry.Circle
import com.inkostilation.pong.engine.geometry.Rectangle
import java.util.*

class Ball(private val defaultX: Float, private val defaultY: Float) : Circle(defaultX, defaultY, 10f), IUpdatable {
    private var velocity = 80f
    private var xVel: Float
    private var yVel: Float

    enum class Direction(val value: Int) {
        LEFT(-1), RIGHT(1);

    }

    fun resetPosition() {
        x = defaultX
        y = defaultY
    }

    fun resetVelocity(direction: Direction) {
        yVel = 0f
        xVel = velocity * direction.value
    }

    private val randomDirection: Int
        private get() = if (Random().nextBoolean()) 1 else -1

    fun applyCollision(rectangle: Rectangle) {
        val relativeIntersectY = rectangle.y + rectangle.height / 2 - y
        val normalizedRelativeIntersectionY = 2 * relativeIntersectY / rectangle.height
        val bounceAngle = normalizedRelativeIntersectionY * MAXBOUNCEANGLE
        val newDir = if (xVel > 0) -1 else 1
        xVel = (newDir * velocity * Math.cos(bounceAngle.toDouble())).toFloat()
        yVel = (-velocity * Math.sin(bounceAngle.toDouble())).toFloat()
    }

    override fun update(delta: Float) {
        x = x + xVel * delta
        y = y + yVel * delta
    }

    fun constrain(limit: Rectangle) {
        yVel = -yVel
        if (y - radius < limit.y) {
            y = limit.y + radius
        } else if (y + radius > limit.y + limit.height) {
            y = limit.y + limit.height - radius
        }
        if (x - radius < limit.x) {
            x = limit.x + radius
        } else if (x + radius > limit.x + limit.width) {
            x = limit.x + limit.height - radius
        }
    }

    fun setVelocity(velocity: Float) {
        this.velocity = velocity
    }

    companion object {
        private const val MAXBOUNCEANGLE = (5 * Math.PI / 12).toFloat()
    }

    init {
        xVel = velocity * randomDirection
        yVel = velocity * randomDirection
    }
}