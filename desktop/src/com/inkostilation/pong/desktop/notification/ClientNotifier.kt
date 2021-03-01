package com.inkostilation.pong.desktop.notification

import com.inkostilation.pong.notifications.INotifier
import com.inkostilation.pong.notifications.IObserver
import java.util.*
import java.util.function.Consumer

class ClientNotifier private constructor() : INotifier {
    private val observers: MutableMap<Class<*>?, MutableList<IObserver<*>?>?> = HashMap()
    override fun <O> notifyObservers(vararg observable: O) {
        if (observable != null) {
            val observableClass = observable.javaClass.componentType as Class<O>
            if (observers.containsKey(observableClass)) {
                observers[observableClass]!!.forEach(Consumer { o: IObserver<*> -> o.observe(*observable) })
            }
        }
    }

    override fun <O> subscribe(observer: IObserver<O>?, type: Class<O>?) {
        if (observers.containsKey(type)) {
            observers[type]!!.add(observer)
        } else {
            val list: MutableList<IObserver<*>?> = ArrayList()
            list.add(observer)
            observers[type] = list
        }
    }

    override fun <O> unsubscribe(observer: IObserver<O>?, type: Class<O>?) {
        if (observers.containsKey(type) && observers[type]!!.contains(observer)) {
            observers[type]!!.remove(observer)
        }
    }

    companion object {
        var instance: INotifier? = null
            get() {
                if (field == null) {
                    field = ClientNotifier()
                }
                return field
            }
            private set
    }
}