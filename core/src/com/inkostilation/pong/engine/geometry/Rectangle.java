package com.inkostilation.pong.engine.geometry;

public class Rectangle extends AbstractGeometricShape {

    private float width, height;

    public Rectangle (float x, float y, float width, float height)
    {
        super(x,y);
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    @Override
    public boolean isInBounds(Rectangle field) {
        if ((getY() < 0) || (getY() > field.height - height))
            return false;
        return true;
    }

}
