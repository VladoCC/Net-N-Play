package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

interface IShape {
    fun draw(rect: DrawRect?, renderer: ShapeRenderer): DrawRect?
    fun addChild(shape: IShape)
    val children: List<IShape>
    val childrenRect: DrawRect
    val isReady: Boolean
    fun drawShapeTree(renderer: ShapeRenderer) {
        drawShapeTree(DrawRect(Position(0, 0), Position(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())), renderer)
    }

    fun drawShapeTree(rect: DrawRect?, renderer: ShapeRenderer): DrawRect? {
        if (isReady) {
            val tmp = draw(rect, renderer)
            var childrenPos = childrenRect.getNewRect(rect!!.bottomLeft!!.x, rect.bottomLeft!!.y)
            for (shape in children) {
                childrenPos = shape.drawShapeTree(childrenPos, renderer)
            }
            return tmp
        }
        return rect
    }

    class Position(val x: Float, val y: Float) {

        fun getNewPosition(moveX: Float, moveY: Float): Position {
            return Position(x + moveX, y + moveY)
        }

    }

    class DrawRect(val bottomLeft: Position?, val topRight: Position?) {

        fun getNewRect(moveX: Float, moveY: Float): DrawRect {
            return DrawRect(bottomLeft!!.getNewPosition(moveX, moveY),
                    topRight!!.getNewPosition(moveX, moveY))
        }

    }
}