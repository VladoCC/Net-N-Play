package com.inkostilation.pong.engine;

import com.inkostilation.pong.engine.geometry.Rectangle;

public class Paddle extends Rectangle implements IUpdatable {

    private static final float FRICTION = 0.07f;
    private static final float ACCELERATION = 40f;
    private static final float MAX_VELOCITY = 90f;

    private float yVel, defaultX, defaultY;

    private PlayerRole playerRole;

    private boolean controlled;

    private Direction accelerationDirection;

    public Paddle(float x, float y) {
        super(x, y, 20, 80);
        defaultX = x;
        defaultY = y;
        setDefaultState();
    }

    public void reset() {
        setX(defaultX);
        setY(defaultY);
        yVel = 0;
        setDefaultState();
    }

    private void setDefaultState() {
        this.yVel = 0;
        accelerationDirection = Direction.IDLE;
        this.controlled = false;
    }

    public void setAccelerationDirection(Direction accelerationDirection) {
        this.accelerationDirection = accelerationDirection;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) { this.playerRole = playerRole; }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

    public boolean isControlled() {
        return controlled;
    }

    @Override
    public void update(float delta) {
        int dir = accelerationDirection.value;
        if (dir != 0)
            yVel += ACCELERATION * dir * delta;
        else
            yVel *= (1 - FRICTION * delta);

        if (yVel >= MAX_VELOCITY)
            yVel = MAX_VELOCITY;
        else if (yVel <= -MAX_VELOCITY)
            yVel = -MAX_VELOCITY;

        setY(getY() + yVel * delta);
    }

    public void constrain(Rectangle rectangle)
    {
        if (getY() < 0) {
            setY(0);
        }
        else if (getY() > rectangle.getHeight() - getHeight()) {
            setY(rectangle.getHeight() - getHeight());
        }
    }
}
