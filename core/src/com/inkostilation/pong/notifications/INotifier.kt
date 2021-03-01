package com.inkostilation.pong.notifications

interface INotifier {
    fun <O> notifyObservers(vararg observable: O)
    fun <O> subscribe(observer: IObserver<O>?, type: Class<O>?)
    fun <O> unsubscribe(observer: IObserver<O>?, type: Class<O>?)
}