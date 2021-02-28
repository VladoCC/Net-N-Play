package com.inkostilation.pong.desktop.notification;

import com.inkostilation.pong.notifications.INotifier;
import com.inkostilation.pong.notifications.IObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientNotifier implements INotifier {

    private Map<Class, List<IObserver>> observers = new HashMap<>();

    private static INotifier instance = null;

    private ClientNotifier() {
    }

    public static INotifier getInstance() {
        if (instance == null) {
            instance = new ClientNotifier();
        }
        return instance;
    }

    @Override
    public <O> void notifyObservers(O... observable) {
        if (observable != null) {
            Class<O> observableClass = (Class<O>) observable.getClass().getComponentType();;
            if (observers.containsKey(observableClass)) {
                observers.get(observableClass).forEach(o -> o.observe(observable));
            }
        }
    }

    @Override
    public <O> void subscribe(IObserver<O> observer, Class<O> type) {
        if (observers.containsKey(type)) {
            observers.get(type).add(observer);
        } else {
            List<IObserver> list = new ArrayList<>();
            list.add(observer);
            observers.put(type, list);
        }
    }

    @Override
    public <O> void unsubscribe(IObserver<O> observer, Class<O> type) {
        if (observers.containsKey(type) && observers.get(type).contains(observer)) {
            observers.get(type).remove(observer);
        }
    }
}
