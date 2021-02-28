package com.inkostilation.pong.desktop.display.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public interface IShape {

    DrawRect draw(DrawRect rect, ShapeRenderer renderer);

    void addChild(IShape shape);

    List<IShape> getChildren();

    DrawRect getChildrenRect();

    boolean isReady();

    default void drawShapeTree(ShapeRenderer renderer) {
        drawShapeTree(new DrawRect(new Position(0,0), new Position(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())), renderer);
    }

    default DrawRect drawShapeTree(DrawRect rect, ShapeRenderer renderer) {
        if (isReady()) {
            DrawRect tmp = draw(rect, renderer);
            DrawRect childrenPos = getChildrenRect().getNewRect(rect.getBottomLeft().getX(), rect.getBottomLeft().getY());
            for (IShape shape : getChildren()) {
                childrenPos = shape.drawShapeTree(childrenPos, renderer);
            }
            return tmp;
        }
        return rect;
    }

    class Position {
        private float x;
        private float y;

        public Position(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public Position getNewPosition(float moveX, float moveY) {
            return new Position(x + moveX, y + moveY);
        }
    }

    class DrawRect {
        private Position bottomLeft;
        private Position topRight;

        public DrawRect(Position bottomLeft, Position topRight) {
            this.bottomLeft = bottomLeft;
            this.topRight = topRight;
        }

        public Position getBottomLeft() {
            return bottomLeft;
        }

        public Position getTopRight() {
            return topRight;
        }

        public DrawRect getNewRect(float moveX, float moveY) {
            return new DrawRect(bottomLeft.getNewPosition(moveX, moveY),
                    topRight.getNewPosition(moveX, moveY));
        }
    }
}
