package com.inkostilation.pong.desktop.display

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.inkostilation.pong.commands.ExitGameCommand
import com.inkostilation.pong.commands.request.RequestInputActionCommand
import com.inkostilation.pong.commands.request.RequestReadinessCommand
import com.inkostilation.pong.commands.request.RequestUpdateCommand
import com.inkostilation.pong.desktop.controls.InputSystem
import com.inkostilation.pong.desktop.display.shapes.*
import com.inkostilation.pong.desktop.network.Network
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.engine.GameState
import com.inkostilation.pong.engine.PlayerRole
import com.inkostilation.pong.exceptions.NoEngineException
import com.inkostilation.pong.notifications.IObserver
import java.io.IOException

class PongScreen : Screen, IObserver<GameState?> {
    private val shapeRenderer = ShapeRenderer()
    private var rootShape: IShape? = null
    private var state: GameState? = null
    private var ready = false
    override fun show() {
        ClientNotifier.Companion.getInstance().subscribe(this, GameState::class.java)
        rootShape = ContainerShapes(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        rootShape.addChild(ScoreShape())
        val field = FieldShape()
        rootShape.addChild(field)
        field.addChild(BallShape())
        field.addChild(PaddleShape(PlayerRole.FIRST))
        field.addChild(PaddleShape(PlayerRole.SECOND))
        Gdx.input.inputProcessor = InputSystem()
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = Color.BLACK
        rootShape!!.drawShapeTree(shapeRenderer)
        shapeRenderer.end()
        try {
            val system = Gdx.input.inputProcessor as InputSystem
            if (system.isChanged) {
                if (!ready) {
                    ready = true
                    Network.getEngine().sendCommand(RequestReadinessCommand<Any?>())
                } else {
                    Network.getEngine().sendCommand(RequestInputActionCommand<Any?>(system.direction))
                }
            } else {
                Network.getEngine().sendCommand(RequestUpdateCommand<Any?>())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoEngineException) {
            e.printStackTrace()
        }
        if (state === GameState.INACTIVE) {
            (Gdx.app.applicationListener as Game).screen = LobbyScreen()
        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {
        dispose()
    }

    override fun dispose() {
        shapeRenderer.dispose()
        ClientNotifier.Companion.getInstance().unsubscribe(this, GameState::class.java)
        try {
            Network.getEngine().sendCommand(ExitGameCommand<Any?>())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoEngineException) {
            e.printStackTrace()
        }
    }

    override fun observe(vararg observable: GameState) {
        state = observable[0]
        if (state === GameState.WAITING) {
            ready = false
        }
    }
}