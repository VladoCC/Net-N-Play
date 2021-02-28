package com.inkostilation.pong.commands;

import com.inkostilation.pong.notifications.INotifier;

public abstract class AbstractResponseCommand implements ICommand {

    private INotifier notifier;

    public AbstractResponseCommand() {
    }

    protected INotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(INotifier notifier) {
        this.notifier = notifier;
    }
}
