package com.inkostilation.pong.engine;

public enum GameState {
    WAITING(0, "Waiting"),
    PREPARATION(1, "Almost ready..."),
    PLAYING(2, "Playing"),
    INACTIVE(3, "Interrupted"),
    AFTER_GOAL_CONFIRMATION(4, "Just scored a goal");

    public final int state;
    public final String name;

    GameState(int state, String name) {
        this.state = state;
        this.name = name;
    }
}
