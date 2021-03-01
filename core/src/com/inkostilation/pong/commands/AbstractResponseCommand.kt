package com.inkostilation.pong.commands

import com.inkostilation.pong.notifications.INotifier

abstract class AbstractResponseCommand<I> : ICommand<I, Unit> {
    var notifier: INotifier? = null protected get() {
        return field
    }
        set

}