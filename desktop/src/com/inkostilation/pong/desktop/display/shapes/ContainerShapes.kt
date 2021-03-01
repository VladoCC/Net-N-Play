package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class ContainerShapes(private val width: Float, private val height: Float) : AbstractShape() {
    override fun draw(rect: IShape.DrawRect?, renderer: ShapeRenderer): IShape.DrawRect? {
        return rect!!.getNewRect(width, height)
    }

    init {
        isReady = true
    }
}