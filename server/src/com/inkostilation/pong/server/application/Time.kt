package com.inkostilation.pong.server.application

object Time {
    private var lastTime = System.nanoTime()
    val deltaTime: Float
        get() {
            val delta = System.nanoTime() - lastTime
            return delta / 1000000000f
        }

    fun updateTime() {
        lastTime = System.nanoTime()
    }
}