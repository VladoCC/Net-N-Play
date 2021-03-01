package com.inkostilation.pong.notifications

interface IObserver<O> {
    fun observe(vararg observable: O)
}