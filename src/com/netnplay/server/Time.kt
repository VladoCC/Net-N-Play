package com.netnplay.server

/**
 * Special object for counting time and getting server-specific deltas
 */
object Time {

    private var lastTime = System.nanoTime()
    private const val nanoToSec = 1000000000f

    /**
     * Variable that returns time passed from the last call to [updateTime].
     * Used for getting delta time from the last processing cycle from any place.
     */
    val deltaTime: Float
        get() {
            val delta = System.nanoTime() - lastTime
            return delta / nanoToSec
        }

    /**
     * Updates time variable to current time and resets delta time
     */
    fun updateTime() {
        lastTime = System.nanoTime()
    }
}