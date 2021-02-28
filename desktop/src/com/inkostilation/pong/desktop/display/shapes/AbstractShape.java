package com.inkostilation.pong.desktop.display.shapes;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractShape implements IShape {

    private List<IShape> children = new ArrayList<>();

    private boolean ready = false;

    @Override
    public void addChild(IShape shape) {
        children.add(shape);
    }

    @Override
    public List<IShape> getChildren() {
        return children;
    }

    @Override
    public DrawRect getChildrenRect() {
        return new DrawRect(new Position(0, 0), new Position(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
