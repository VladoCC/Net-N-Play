package com.inkostilation.pong.notifications;

import com.inkostilation.pong.engine.Ball;
import com.inkostilation.pong.engine.Field;
import com.inkostilation.pong.engine.Paddle;

public interface INotifier {

    <O> void notifyObservers(O... observable);
    <O> void subscribe(IObserver<O> observer, Class<O> type);
    <O> void unsubscribe(IObserver<O> observer, Class<O> type);
}
