package com.inkostilation.pong.desktop.network;

import java.io.IOException;

public class ServerThread extends Thread {

    private boolean active = false;
    private boolean finished = false;

    private static ServerThread instance;

    public static ServerThread getInstance() {
        if (instance == null) {
            instance = new ServerThread();
        }
        return instance;
    }

    @Override
    public synchronized void start() {
        active = true;
        super.start();
    }

    @Override
    public void run() {
        while (active) {
            Network.getEngine().act(0);
        }

        // last messages to disconnect and etc.
        try {
            Network.getEngine().quit(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        finished = true;
    }

    public void stopServer() {
        this.active = false;
    }

    public boolean isFinished() {
        return finished;
    }
}
