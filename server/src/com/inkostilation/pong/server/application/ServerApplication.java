package com.inkostilation.pong.server.application;

import com.inkostilation.pong.server.engine.PongEngine;
import com.inkostilation.pong.server.network.NetworkProcessor;

public class ServerApplication {

    private NetworkProcessor processor;

    public ServerApplication() {
        processor = NetworkProcessor.getInstance();
        processor.start();

        loop();
    }

    private void loop() {
        while (true) {
            if (processor.isStarted()) {
                processor.processConnections();
                PongEngine.getInstance().act(Time.getDeltaTime());
                Time.updateTime();
            }
        }
    }
}
