package com.inkostilation.pong.server.application;

public class Time {

    private static long lastTime = System.nanoTime();

    public static float getDeltaTime() {
        long time = System.nanoTime();
        long delta = time - lastTime;
        return delta / 1000000000f;
    }

    public static void updateTime() {
        lastTime = System.nanoTime();
    }
}
