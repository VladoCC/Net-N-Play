package com.inkostilation.pong.desktop.display.shapes

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.PlayerRole
import com.inkostilation.pong.engine.Score
import com.inkostilation.pong.notifications.IObserver

class ScoreShape : AbstractShape(), IObserver<Score?> {
    private var observable: Score? = null
    override fun draw(rect: IShape.DrawRect?, renderer: ShapeRenderer): IShape.DrawRect? {
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        renderer.set(ShapeRenderer.ShapeType.Filled)
        renderer.color = Color.WHITE
        renderer.rect(0f, h - height, w, 8f)
        renderer.rect(0f, h - 8, w, 8f)
        renderer.rect(w / 2 - 4, h - height, 8f, height)
        val count = observable!!.maxScoreValue
        drawScore(observable!!.getPlayerScore(PlayerRole.FIRST), count, IShape.Position(w / 12, h - height / 2 - pointDim / 2), w / 2 - w / 6, renderer)
        drawScore(observable!!.getPlayerScore(PlayerRole.SECOND), count, IShape.Position(w / 2 + w / 12, h - height / 2 - pointDim / 2), w / 2 - w / 6, renderer)
        return IShape.DrawRect(rect.getBottomLeft(), rect.getTopRight().getNewPosition(0f, -height))
    }

    private fun drawScore(score: Int, maxScore: Int, startPos: IShape.Position, width: Float, renderer: ShapeRenderer) {
        val step = (width - pointDim) / (maxScore - 1)
        for (i in 0 until maxScore) {
            if (i < score) {
                renderer.color = Color.WHITE
            } else {
                renderer.color = Color.GRAY
            }
            renderer.rect(startPos.x + step * i, startPos.y, pointDim, pointDim)
        }
    }

    override fun observe(vararg observable: Score) {
        println("Score observed")
        if (observable != null) {
            this.observable = observable[0]
            isReady = true
        }
    }

    companion object {
        private const val height = 100f
        private const val pointDim = 20f
    }

    init {
        ClientNotifier.Companion.getInstance().subscribe(this, Score::class.java)
    }
}