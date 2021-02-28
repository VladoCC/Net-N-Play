package com.inkostilation.pong.notifications;

public interface IObserver<O> {

    void observe(O... observable);
}
