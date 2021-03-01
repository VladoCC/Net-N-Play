package com.inkostilation.pong.engine.geometry

abstract class AbstractGeometricShape(var x: Float, var y: Float) {

    abstract fun isInBounds(rectangle: Rectangle): Boolean

}