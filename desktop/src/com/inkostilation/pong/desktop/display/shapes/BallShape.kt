package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.Ball
import com.inkostilation.pong.notifications.IObserver

class BallShape : AbstractShape(), IObserver<Ball?> {
    private var observable: Ball? = null
    override fun draw(rect: IShape.DrawRect?, renderer: ShapeRenderer): IShape.DrawRect? {
        val radius = observable!!.radius
        val x = observable!!.x + rect.getBottomLeft().x
        val y = observable!!.y + rect.getBottomLeft().y
        renderer.color = Color.WHITE
        renderer.set(ShapeRenderer.ShapeType.Filled)
        renderer.circle(x, y, radius)
        return rect
    }

    override fun observe(vararg observable: Ball) {
        this.observable = observable[0]
        isReady = true
    }

    init {
        ClientNotifier.Companion.getInstance().subscribe(this, Ball::class.java)
    }
}