package com.inkostilation.pong.desktop.display

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.inkostilation.pong.commands.request.RequestConnectionCommand
import com.inkostilation.pong.commands.request.RequestGameListCommand
import com.inkostilation.pong.commands.request.RequestNewGameCommand
import com.inkostilation.pong.desktop.network.Network
import com.inkostilation.pong.engine.PongGame
import com.inkostilation.pong.exceptions.NoEngineException
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.util.adapter.ArrayListAdapter
import com.kotcrab.vis.ui.widget.*
import com.kotcrab.vis.ui.widget.ListView.ItemClickListener
import java.io.IOException
import java.util.*

class LobbyScreen : Screen {
    private var view: Stage? = null
    private val multiplexer = InputMultiplexer()
    private val list = ArrayList<Array<String>>()
    override fun show() {
        if (!VisUI.isLoaded()) {
            VisUI.load(VisUI.SkinScale.X1)
        }
        view = Stage(ScreenViewport())
        Gdx.input.inputProcessor = view
        list.add(arrayOf("test", "second", "123"))
        list.add(arrayOf("1111", "----", "\\./"))
        list.add(arrayOf("test", "second", "123"))
        val test: ArrayListAdapter<Array<String>, HorizontalGroup> = object : ArrayListAdapter<Array<String?>?, HorizontalGroup?>(list) {
            protected override fun createView(item: Array<String?>): HorizontalGroup {
                val table = HorizontalGroup()
                for (i in item) {
                    val label = VisLabel(i)
                    table.addActor(label)
                }
                return table
            }
        }
        val table = VisTable()
        table.setFillParent(true)
        view!!.addActor(table)
        table.row().fill().expandY()
        val tree: ObserverTree<VisTable?, PongGame?> = object : ObserverTree<VisTable, PongGame>(PongGame::class.java) {
            public override fun toActor(data: PongGame): VisTable {
                val treeTable = VisTable()
                treeTable.row().expandX().fillX()
                treeTable.add(VisLabel(data.getGameState().name)).padRight(300f)
                treeTable.add(VisLabel(data.playersNumber.toString() + ""))
                return treeTable
            }
        }
        val view = ListView(test)
        view.setItemClickListener(ItemClickListener<Array<String?>?> { item -> println(Arrays.toString(item)) })
        val group = VerticalGroup()
        group.addActor(VisLabel("test"))
        group.pack()
        view.header = group
        val listWindow = VisWindow("Lobby")
        val listTable = VisTable()
        listTable.setFillParent(true)
        listTable.row().padTop(20f)
        listTable.add(group)
        listTable.row().expandY().expandX().fillX().fill().align(Align.center)
        listTable.add<ObserverTree<VisTable, PongGame>>(tree)
        listTable.pack()
        listTable.align(Align.top)
        listWindow.addActor(listTable)
        listWindow.isMovable = false
        listWindow.titleTable.removeActor(listWindow.titleLabel)
        listWindow.titleTable.row()
        listWindow.titleTable.add(VisLabel("Lobby")).center()
        listWindow.titleTable.pack()
        view.rebuildView()
        table.add(listWindow).padTop(10f).expandY().fill()
        table.row()
        val bottomTable = VisTable()
        table.add(bottomTable)
        bottomTable.bottom().padRight(100f).padTop(10f).padBottom(10f)
        val refreshButton = VisTextButton("Refresh")
        refreshButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                try {
                    Network.getEngine().sendCommand(RequestGameListCommand<Any?>())
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: NoEngineException) {
                    e.printStackTrace()
                }
            }
        })
        val connectButton = VisTextButton("Connect")
        connectButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                try {
                    if (tree.selection.lastSelected != null) {
                        val index = tree.selection.lastSelected.pos
                        (Gdx.app.applicationListener as Game).screen = PongScreen()
                        Network.getEngine().sendCommand(RequestConnectionCommand<Any?>(index))
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: NoEngineException) {
                    e.printStackTrace()
                }
            }
        })
        val newButton = VisTextButton("New")
        newButton.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                try {
                    (Gdx.app.applicationListener as Game).screen = PongScreen()
                    Network.getEngine().sendCommand(RequestNewGameCommand<Any?>())
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: NoEngineException) {
                    e.printStackTrace()
                }
            }
        })
        bottomTable.add(refreshButton).padLeft(50f)
        bottomTable.add(connectButton).padLeft(50f)
        bottomTable.add(newButton).padLeft(50f)
        //table.pack();
        try {
            Network.getEngine().sendCommand(RequestGameListCommand<Any?>())
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoEngineException) {
            e.printStackTrace()
        }
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        view!!.act()
        view!!.draw()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
    override fun dispose() {}
}