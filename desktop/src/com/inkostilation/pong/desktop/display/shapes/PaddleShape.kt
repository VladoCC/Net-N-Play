package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.Paddle
import com.inkostilation.pong.engine.PlayerRole
import com.inkostilation.pong.notifications.IObserver

class PaddleShape(role: PlayerRole) : AbstractShape(), IObserver<Paddle?> {
    private var observable: Paddle? = null
    private val role: PlayerRole
    override fun draw(rect: IShape.DrawRect?, renderer: ShapeRenderer): IShape.DrawRect? {
        if (observable!!.isControlled) {
            renderer.color = Color.WHITE
        } else {
            renderer.color = Color.GRAY
        }
        renderer.set(ShapeRenderer.ShapeType.Filled)
        renderer.rect(rect.getBottomLeft().x + observable!!.x, rect.getBottomLeft().y + observable!!.y,
                observable!!.width, observable!!.height)
        return rect
    }

    override fun observe(vararg observable: Paddle) {
        val first = observable[0]
        if (first.playerRole != null && first.playerRole == role) {
            this.observable = first
            isReady = true
        }
    }

    init {
        ClientNotifier.Companion.getInstance().subscribe(this, Paddle::class.java)
        this.role = role
    }
}