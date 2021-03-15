package com.inkostilation.pong.server.engine

import java.util.*

class MainEngine: AbstractEngine() {
    override fun prepare() {
    }

    override fun start() {
    }

    override fun act(delta: Float) {
    }

    override fun enter(marker: UUID) {
    }

    override fun quit(marker: UUID) {
    }

    override fun stop() {
    }

    var counter = 0
        get() {
            val res = field
            field++
            return res
        }
}