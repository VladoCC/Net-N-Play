package com.inkostilation.pong.engine;

public enum Direction {
    UP(1), DOWN(-1), IDLE(0);

    public final int value;

    Direction(int dir) {
        value = dir;
    }
}
