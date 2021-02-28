package com.inkostilation.pong.desktop.controls;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.inkostilation.pong.engine.Direction;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class InputSystem extends InputAdapter {

    private Direction direction = Direction.IDLE;
    private Set<Direction> set = new LinkedHashSet<>();

    private boolean changed = false;

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                set.add(Direction.UP);
                changed = true;
                break;
            case Input.Keys.DOWN:
                set.add(Direction.DOWN);
                changed = true;
                break;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                set.remove(Direction.UP);
                changed = true;
                break;
            case Input.Keys.DOWN:
                set.remove(Direction.DOWN);
                changed = true;
                break;
        }
        return super.keyUp(keycode);
    }

    public Direction getDirection() {
        if (set.size() > 0) {
            Direction[] dir = new Direction[set.size()];
            set.toArray(dir);
            return dir[set.size() - 1];
        }
        changed = false;
        return Direction.IDLE;
    }

    public boolean isChanged() {
        return changed;
    }
}
