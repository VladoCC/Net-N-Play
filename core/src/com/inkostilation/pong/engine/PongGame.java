package com.inkostilation.pong.engine;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.commands.response.ResponseGameStateCommand;

import java.util.*;

public class PongGame implements IUpdatable, ICommandSender {

    private static final float AFTER_GOAL_TIME = 3.0F;
    private static final float PREP_TIME = 3.0F;

    private Field field;
    private Score score;
    private GameState gameState = GameState.INACTIVE;
    private int playersNumber;
    private PlayerRole winner;
    private boolean active = true;

    private boolean changed = true;

    private float timer = 0;

    private LinkedList<PlayerRole> roles = new LinkedList<>();

    private static ListMultimap<GameState, PongGame> activeGamesMap = ArrayListMultimap.create();

    public PongGame()
    {
        this.field = new Field();
        this.score = new Score();
        this.playersNumber = 0;
        this.gameState = GameState.WAITING;
        activeGamesMap.put(gameState, this);

        roles.add(PlayerRole.FIRST);
        roles.add(PlayerRole.SECOND);
        roles.add(PlayerRole.DENIED);
    }

    public static PongGame getWaitingGame(int index) {
        if (!activeGamesMap.isEmpty() && !getGameCollection(GameState.WAITING).isEmpty()) {
            PongGame tempGame = getGame(GameState.WAITING, index);
            return tempGame;
        } else {
            PongGame newGame = new PongGame();
            return newGame;
        }
    }

    public Field getField() {
        return field;
    }

    public Score getScore() {
        return score;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        if (gameState != GameState.INACTIVE) {
            activeGamesMap.remove(this.gameState, this);
            activeGamesMap.put(gameState, this);
        }

        changed = true;
        this.gameState = gameState;
    }

    public PlayerRole addPlayer() {
        ++this.playersNumber;
        PlayerRole role = roles.size() > 1? roles.pop() : roles.peek();
        return role;
    }

    public void removePlayer(PlayerRole role) {
        --this.playersNumber;

        if (role != PlayerRole.DENIED) {
            roles.push(role);
            setGameState(GameState.WAITING);
            field.reset();
        }

        if (playersNumber == 0) {
            stop();
        }
    }

    public void start() {
        //active = true;

        setGameState(GameState.PREPARATION);
    }
    
    public void stop() {
        activeGamesMap.remove(gameState, this);
        active = false;
        setGameState(GameState.INACTIVE);
    }

    public void scorePoint(PlayerRole role) {
        score.addPlayerScore(role, 1);
    }

    @Override
    public void update(float delta) {
        if (active) {
            if (gameState == GameState.AFTER_GOAL_CONFIRMATION) {
                timer += delta;
                if (timer >= AFTER_GOAL_TIME) {
                    if (score.getMaxValueCount() == 0) {
                        field.reset();
                        setGameState(GameState.WAITING);
                    } else {
                        winner = score.getMaxedPlayers().get(0);
                        stop();
                    }
                }
            } else if (gameState == GameState.PREPARATION) {
                timer += delta;
                if (timer >= PREP_TIME) {
                    field.setStarted(true);
                    setGameState(GameState.PLAYING);
                    timer = 0;
                }
            }

            field.update(delta);
            if (!field.isBallInBounds() && gameState == GameState.PLAYING) {
                scorePoint(field.getBall().getX() < 0 ? PlayerRole.SECOND : PlayerRole.FIRST);
                setGameState(GameState.AFTER_GOAL_CONFIRMATION);
                timer = 0;
            }
        }
    }

    public static List<PongGame> getGameCollection(GameState state) {
        return activeGamesMap.get(state);
    }

    public static PongGame getGame(GameState state, int index) {
        return getGameCollection(state).get(index);
    }

    public static ListMultimap<GameState, PongGame> getActiveGamesMap() {
        return activeGamesMap;
    }

    public void setControlled(PlayerRole playerRole, boolean state) {
        getField().setControlled(playerRole, state);
    }

    public boolean isControlled(PlayerRole playerRole) {
        return getField().isControlled(playerRole);
    }

    public Paddle getPaddle(PlayerRole playerRole) {
        return getField().getPaddle(playerRole);
    }

    public void setPlayerRole(PlayerRole playerRole) {
        getField().getPaddle(playerRole).setPlayerRole(playerRole);
    }

    @Override
    public boolean hasCommands() {
        return changed || field.hasCommands() || score.hasCommands();
    }

    @Override
    public void getCommands(List<AbstractResponseCommand> pool) {
        if (changed) {
            changed = false;
            pool.add(new ResponseGameStateCommand(gameState));
        }

        field.getCommands(pool);
        score.getCommands(pool);
    }
}
