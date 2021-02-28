package com.inkostilation.pong.server;

import com.inkostilation.pong.server.application.ServerApplication;
import com.inkostilation.pong.server.engine.PongEngine;
import com.inkostilation.pong.server.network.NetworkProcessor;

import java.io.IOException;

public class ServerLauncher {
	public static void main (String[] arg) throws IOException {
		new ServerApplication();
	}
}