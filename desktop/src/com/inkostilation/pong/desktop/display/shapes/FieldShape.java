package com.inkostilation.pong.desktop.display.shapes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.inkostilation.pong.desktop.notification.ClientNotifier;
import com.inkostilation.pong.engine.Field;
import com.inkostilation.pong.notifications.IObserver;

public class FieldShape extends AbstractShape implements IObserver<Field> {

    private Field observable;

    private float offsetX = 0, offsetY = 0;

    private final static int WIDTH = 3;

    public FieldShape() {
        ClientNotifier.getInstance().subscribe(this, Field.class);
    }

    @Override
    public DrawRect draw(DrawRect rect, ShapeRenderer renderer) {
        float xEnd = rect.getTopRight().getX();
        float yEnd = rect.getTopRight().getY();

        float width = observable.getWidth();
        float height = observable.getHeight();

        offsetX = (xEnd - width - rect.getBottomLeft().getX()) / 2;
        offsetY = (yEnd - height - rect.getBottomLeft().getY()) / 2;

        float x = rect.getBottomLeft().getX() + offsetX;
        float y = rect.getBottomLeft().getY() + offsetY;

        renderer.setColor(Color.WHITE);
        renderer.rectLine(x, y + 2, x + width, y + 2, 4);
        renderer.rectLine(x, y + height - 2, x + width, y + height - 2, 4);
        renderer.rectLine(x + width / 2, y, x + width / 2, y + height / 4, 4);
        renderer.rectLine(x + width / 2, y + 3 * height / 4, x + width / 2, y + height, 4);
        renderer.circle(x + width / 2, y + height / 2, height / 4 + 2);
        renderer.setColor(Color.BLACK);
        renderer.circle(x + width / 2, y + height / 2, height / 4 - 2);
        renderer.setColor(Color.WHITE);
        renderer.circle(x + width / 2, y + height / 2,  4);
        return new DrawRect(new Position(rect.getBottomLeft().getX(), yEnd), rect.getTopRight().getNewPosition(0, 0));
    }

    @Override
    public void observe(Field... observable) {
        this.observable = observable[0];
        setReady(true);
    }

    @Override
    public DrawRect getChildrenRect() {
        return new DrawRect(new Position(offsetX, offsetY), new Position(offsetX + observable.getWidth(), offsetY + observable.getHeight()));
    }
}
