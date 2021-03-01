package com.inkostilation.pong.engine

enum class GameState(val state: Int, override val name: String) {
    WAITING(0, "Waiting"), PREPARATION(1, "Almost ready..."), PLAYING(2, "Playing"), INACTIVE(3, "Interrupted"), AFTER_GOAL_CONFIRMATION(4, "Just scored a goal");

}