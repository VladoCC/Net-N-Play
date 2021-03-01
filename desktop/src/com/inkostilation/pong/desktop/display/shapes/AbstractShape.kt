package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.Gdx
import java.util.*

abstract class AbstractShape : IShape {
    private override val children: MutableList<IShape> = ArrayList()
    override var isReady: Boolean = false public get() {
        return field
    }

    override fun addChild(shape: IShape) {
        children.add(shape)
    }

    override fun getChildren(): List<IShape> {
        return children
    }

    override val childrenRect: IShape.DrawRect
        get() = IShape.DrawRect(IShape.Position(0, 0), IShape.Position(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat()))

}