package com.inkostilation.pong.engine;

public class PlayerData {

    private PongGame game;
    private PlayerRole playerRole;


    public PlayerData(PongGame game, PlayerRole playerRole) {
        this.game = game;
        this.playerRole = playerRole;
    }

    public PongGame getGame() {
        return game;
    }

    public void setGame(PongGame game) {
        this.game = game;
    }

    public PlayerRole getPlayerRole() {
        return playerRole;
    }

    public void setPlayerRole(PlayerRole playerRole) {
        this.playerRole = playerRole;
    }
}
