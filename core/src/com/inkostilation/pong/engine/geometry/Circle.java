package com.inkostilation.pong.engine.geometry;

public class Circle extends AbstractGeometricShape {

    private float radius;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Circle(float x, float y, float radius)
    {
        super(x,y);
        this.radius = radius;
    }

    public boolean isColliding(Rectangle rectangle) {
        return getY() + radius >= rectangle.getY()
                && getY() - radius <= rectangle.getY() + rectangle.getHeight()
                && getX() + radius >= rectangle.getX()
                && getX() - radius <= rectangle.getX() + rectangle.getWidth();
    }

    @Override
    public boolean isInBounds(Rectangle rectangle) {
        float x = rectangle.getX();
        float y = rectangle.getY();
        float width = rectangle.getWidth();
        float height = rectangle.getHeight();

        return getX() >= (x + radius)
                && getX() <= (x + width - radius)
                && getY() >= (y + radius)
                && getY() <= (y + height - radius);
    }

}
