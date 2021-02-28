package com.inkostilation.pong.engine.geometry;

public abstract class AbstractGeometricShape {

    private float x, y;

    public AbstractGeometricShape(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    abstract public boolean isInBounds(Rectangle rectangle);

}
