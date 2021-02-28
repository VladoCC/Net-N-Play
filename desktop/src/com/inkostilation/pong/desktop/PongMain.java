package com.inkostilation.pong.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.inkostilation.pong.desktop.controls.InputSystem;
import com.inkostilation.pong.desktop.display.LobbyScreen;
import com.inkostilation.pong.desktop.display.PongScreen;
import com.inkostilation.pong.desktop.network.Network;
import com.inkostilation.pong.desktop.network.ServerThread;
import com.inkostilation.pong.engine.Ball;
import com.inkostilation.pong.engine.Field;
import com.inkostilation.pong.engine.IEngine;
import com.inkostilation.pong.engine.Paddle;

public class PongMain extends Game {

	@Override
	public void create () {
		setScreen(new LobbyScreen());
		ServerThread.getInstance().start();
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
		ServerThread.getInstance().stopServer();
		while (!ServerThread.getInstance().isFinished()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
