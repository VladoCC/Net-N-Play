package com.inkostilation.pong.desktop.network;

import com.inkostilation.pong.engine.IEngine;

public class Network {

    private static IEngine engine = null;

    public static IEngine getEngine() {
        if (engine == null) {
            engine = new ServerEngine();
        }
        return engine;
    }

}
