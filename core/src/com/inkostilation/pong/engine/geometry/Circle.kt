package com.inkostilation.pong.engine.geometry

open class Circle(x: Float, y: Float, var radius: Float) : AbstractGeometricShape(x, y) {

    fun isColliding(rectangle: Rectangle): Boolean {
        return y + radius >= rectangle.y && y - radius <= rectangle.y + rectangle.height && x + radius >= rectangle.x && x - radius <= rectangle.x + rectangle.width
    }

    override fun isInBounds(rectangle: Rectangle): Boolean {
        val x = rectangle.x
        val y = rectangle.y
        val width = rectangle.width
        val height = rectangle.height
        return getX() >= x + radius && getX() <= x + width - radius && getY() >= y + radius && getY() <= y + height - radius
    }

}