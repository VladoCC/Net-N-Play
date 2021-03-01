package com.inkostilation.pong.engine.geometry

open class Rectangle(x: Float, y: Float, val width: Float, val height: Float) : AbstractGeometricShape(x, y) {

    override fun isInBounds(field: Rectangle): Boolean {
        return if (y < 0 || y > field.height - height) false else true
    }

}