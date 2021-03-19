package com.netnplay.server

object Time {
    private var lastTime = System.nanoTime()
    private const val nanoToSec = 1000000000f
    val deltaTime: Float
        get() {
            val delta = System.nanoTime() - lastTime
            return delta / nanoToSec
        }

    fun updateTime() {
        lastTime = System.nanoTime()
    }
}