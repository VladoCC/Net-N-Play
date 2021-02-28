package com.inkostilation.pong.desktop.display.shapes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.inkostilation.pong.desktop.notification.ClientNotifier;
import com.inkostilation.pong.engine.Paddle;
import com.inkostilation.pong.engine.PlayerRole;
import com.inkostilation.pong.notifications.IObserver;

public class PaddleShape extends AbstractShape implements IObserver<Paddle> {

    private Paddle observable;
    private PlayerRole role;

    public PaddleShape(PlayerRole role) {
        ClientNotifier.getInstance().subscribe(this, Paddle.class);
        this.role = role;
    }

    @Override
    public DrawRect draw(DrawRect rect, ShapeRenderer renderer) {
        if (observable.isControlled()) {
            renderer.setColor(Color.WHITE);
        } else {
            renderer.setColor(Color.GRAY);
        }
        renderer.set(ShapeRenderer.ShapeType.Filled);
        renderer.rect(rect.getBottomLeft().getX() + observable.getX(), rect.getBottomLeft().getY() + observable.getY(),
                observable.getWidth(), observable.getHeight());

        return rect;
    }

    @Override
    public void observe(Paddle... observable) {
        Paddle first = observable[0];
        if (first.getPlayerRole() != null && first.getPlayerRole().equals(role)) {
            this.observable = first;
            setReady(true);
        }
    }
}
