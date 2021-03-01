package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.Field
import com.inkostilation.pong.notifications.IObserver

class FieldShape : AbstractShape(), IObserver<Field?> {
    private var observable: Field? = null
    private var offsetX = 0f
    private var offsetY = 0f
    override fun draw(rect: IShape.DrawRect?, renderer: ShapeRenderer): IShape.DrawRect? {
        val xEnd = rect.getTopRight().x
        val yEnd = rect.getTopRight().y
        val width = observable!!.width
        val height = observable!!.height
        offsetX = (xEnd - width - rect.getBottomLeft().x) / 2
        offsetY = (yEnd - height - rect.getBottomLeft().y) / 2
        val x = rect.getBottomLeft().x + offsetX
        val y = rect.getBottomLeft().y + offsetY
        renderer.color = Color.WHITE
        renderer.rectLine(x, y + 2, x + width, y + 2, 4f)
        renderer.rectLine(x, y + height - 2, x + width, y + height - 2, 4f)
        renderer.rectLine(x + width / 2, y, x + width / 2, y + height / 4, 4f)
        renderer.rectLine(x + width / 2, y + 3 * height / 4, x + width / 2, y + height, 4f)
        renderer.circle(x + width / 2, y + height / 2, height / 4 + 2)
        renderer.color = Color.BLACK
        renderer.circle(x + width / 2, y + height / 2, height / 4 - 2)
        renderer.color = Color.WHITE
        renderer.circle(x + width / 2, y + height / 2, 4f)
        return IShape.DrawRect(IShape.Position(rect.getBottomLeft().x, yEnd), rect.getTopRight().getNewPosition(0f, 0f))
    }

    override fun observe(vararg observable: Field) {
        this.observable = observable[0]
        isReady = true
    }

    override val childrenRect: IShape.DrawRect
        get() = IShape.DrawRect(IShape.Position(offsetX, offsetY), IShape.Position(offsetX + observable!!.width, offsetY + observable!!.height))

    companion object {
        private const val WIDTH = 3
    }

    init {
        ClientNotifier.Companion.getInstance().subscribe(this, Field::class.java)
    }
}