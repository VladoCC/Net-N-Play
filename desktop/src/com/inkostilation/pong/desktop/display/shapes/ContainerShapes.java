package com.inkostilation.pong.desktop.display.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class ContainerShapes extends AbstractShape {

    private float width, height;

    public ContainerShapes(float width, float height) {
        this.width = width;
        this.height = height;
        setReady(true);
    }

    @Override
    public DrawRect draw(DrawRect rect, ShapeRenderer renderer) {
        return rect.getNewRect(width, height);
    }
}
