package com.inkostilation.pong.desktop.controls

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.inkostilation.pong.engine.Direction
import java.util.*

class InputSystem : InputAdapter() {
    val direction = Direction.IDLE
        get() {
            if (set.size > 0) {
                val dir = arrayOfNulls<Direction>(set.size)
                set.toArray(dir)
                return dir[set.size - 1]!!
            }
            isChanged = false
            return Direction.IDLE
        }
    private val set: MutableSet<Direction> = LinkedHashSet()
    var isChanged = false
        private set

    override fun keyDown(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.UP -> {
                set.add(Direction.UP)
                isChanged = true
            }
            Input.Keys.DOWN -> {
                set.add(Direction.DOWN)
                isChanged = true
            }
        }
        return super.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.UP -> {
                set.remove(Direction.UP)
                isChanged = true
            }
            Input.Keys.DOWN -> {
                set.remove(Direction.DOWN)
                isChanged = true
            }
        }
        return super.keyUp(keycode)
    }

}