package com.inkostilation.pong.desktop.display

import com.badlogic.gdx.scenes.scene2d.Actor
import com.inkostilation.pong.desktop.display.ObserverTree.LobbyNode
import com.inkostilation.pong.desktop.notification.ClientNotifier
import com.inkostilation.pong.notifications.IObserver
import com.kotcrab.vis.ui.widget.VisTree

abstract class ObserverTree<A : Actor?, D>(observableClass: Class<D>?) : VisTree<LobbyNode?, A>(), IObserver<D> {
    override fun observe(vararg observable: D) {
        clearChildren()
        var pos = 0
        for (data in observable) {
            add(LobbyNode(toActor(data), pos))
            pos++
        }
    }

    abstract fun toActor(data: D): A
    class LobbyNode(actor: Actor?, val pos: Int) : Node<Any?, Any?, Any?>(actor)

    init {
        ClientNotifier.Companion.getInstance().subscribe(this, observableClass)
    }
}