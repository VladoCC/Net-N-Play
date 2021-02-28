package com.inkostilation.pong.engine;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.commands.response.ResponseFieldCommand;
import com.inkostilation.pong.engine.geometry.Rectangle;

import java.util.List;

public class Field extends Rectangle implements IUpdatable, ICommandSender {

    private Paddle paddle1, paddle2;
    private Ball ball;
    private boolean started;

    public Field() {
        super(0, 0, 700, 500);

        this.paddle1 = new Paddle(20, 210);
        this.paddle2 = new Paddle(660, 210);
        paddle1.setPlayerRole(PlayerRole.FIRST);
        paddle2.setPlayerRole(PlayerRole.SECOND);

        this.ball = new Ball(350, 250);
    }

    public Paddle getPaddle1() {
        return paddle1;
    }

    public Paddle getPaddle2() {
        return paddle2;
    }

    public Ball getBall() {
        return ball;
    }

    public boolean isBallInBounds() {
        return ball.isInBounds(this);
    }

    @Override
    public void update(float delta) {
        paddle1.update(delta);
        paddle2.update(delta);

        if (started) {
            ball.update(delta);
        }

        if (!paddle1.isInBounds(this)) {
            paddle1.constrain(this);
        }

        if (!paddle2.isInBounds(this)) {
            paddle2.constrain(this);
        }

        if (!ball.isInBounds(this)) {
            onBallOutOfBounds();
        }

        if (ball.isColliding(paddle1)) {
            ball.applyCollision(paddle1);
        }

        if (ball.isColliding(paddle2)) {
            ball.applyCollision(paddle2);
        }
    }

    private void onBallOutOfBounds() {
        if (ball.getY() - ball.getRadius() <= getY()
                || ball.getY() + ball.getRadius() >= getY() + getHeight()) {
            if (ball.getX() - ball.getRadius() >= getX() && ball.getX() + ball.getRadius() <= getX() + getWidth()) {
                ball.constrain(this);
            }
        }
    }

    public void reset() {
        paddle1.reset();
        paddle2.reset();

        ball.resetPosition();
        ball.resetVelocity(ball.getX() - ball.getRadius() < getX() ?
                Ball.Direction.LEFT : Ball.Direction.RIGHT);

        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void setControlled(PlayerRole playerRole, boolean state) {
        switch (playerRole) {
            case FIRST: {
                getPaddle1().setControlled(state);
                break;
            }
            case SECOND: {
                getPaddle2().setControlled(state);
                break;
            }
        }
    }

    public boolean isControlled(PlayerRole playerRole) {
        switch (playerRole) {
            case FIRST: {
                return getPaddle1().isControlled();
            }
            case SECOND: {
                return getPaddle2().isControlled();
            }
        }
        return false;
    }

    public Paddle getPaddle(PlayerRole playerRole) {
        switch (playerRole) {
            case FIRST: {
                return getPaddle1();
            }
            case SECOND: {
                return getPaddle2();
            }
        }
        return null;
    }

    @Override
    public boolean hasCommands() {
        return true;
    }

    @Override
    public void getCommands(List<AbstractResponseCommand> pool) {
        pool.add(new ResponseFieldCommand(this));
    }
}
