package com.inkostilation.pong.desktop.display;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.inkostilation.pong.desktop.notification.ClientNotifier;
import com.inkostilation.pong.notifications.IObserver;
import com.kotcrab.vis.ui.widget.VisTree;

public abstract class ObserverTree<A extends Actor, D> extends VisTree<ObserverTree.LobbyNode, A> implements IObserver<D> {

    public ObserverTree(Class<D> observableClass) {
        super();
        ClientNotifier.getInstance().subscribe(this, observableClass);
    }

    @Override
    public void observe(D... observable) {
        clearChildren();
        int pos = 0;
        for (D data: observable) {
            add(new LobbyNode(toActor(data), pos));
            pos++;
        }
    }

    abstract A toActor(D data);

    public static class LobbyNode extends Tree.Node {

        private int pos;

        public LobbyNode(Actor actor, int pos) {
            super(actor);
            this.pos = pos;
        }

        public int getPos() {
            return pos;
        }
    }
}
