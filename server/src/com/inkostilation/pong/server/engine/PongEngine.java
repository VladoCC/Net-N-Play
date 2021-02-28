package com.inkostilation.pong.server.engine;

import com.google.common.collect.ListMultimap;
import com.inkostilation.pong.commands.*;
import com.inkostilation.pong.commands.response.*;
import com.inkostilation.pong.engine.*;
import com.inkostilation.pong.exceptions.NoEngineException;
import com.inkostilation.pong.server.network.NetworkProcessor;
import java.util.*;
import java.io.IOException;
import java.nio.channels.SocketChannel;

public class PongEngine implements IPongEngine<SocketChannel> {

    private static IPongEngine<SocketChannel> instance = null;

    private Map<SocketChannel, PlayerData> playersMap;

    private PongEngine() {
        playersMap = new HashMap<>();
    }

    public static IPongEngine<SocketChannel> getInstance() {
        if (instance == null) {
            instance = new PongEngine();
        }
        return instance;
    }

    @Override
    public void act(float delta) {
        ListMultimap<GameState, PongGame> map = PongGame.getActiveGamesMap();
        PongGame[] games = new PongGame[map.size()];
        games = map.values().toArray(new PongGame[0]);
        Arrays.stream(games).forEach(g -> {
            g.update(delta);

            if (g.hasCommands()) {
                List<AbstractResponseCommand> commands = new ArrayList<>();
                g.getCommands(commands);
                playersMap.entrySet().stream()
                        .filter(e -> e.getValue().getGame() == g)
                        .forEach(e -> commands.forEach(c -> {
                            try {
                                receiveCommand(c, e.getKey());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }));
            }
        });
    }

    @Override
    public void receiveCommand(AbstractResponseCommand command, SocketChannel channel) throws IOException {
        NetworkProcessor.getInstance().sendMessage(command, channel);

    }

    @Override
    public void sendCommand(AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel>... commands) throws IOException, NoEngineException {
        for (AbstractRequestCommand<IEngine<SocketChannel>, SocketChannel> command : commands) {
            command.setEngine(this);
            command.execute();
        }
    }

    @Override
    public void quit(SocketChannel channel) throws IOException {
        removePlayer(channel);
    }

    @Override
    public void sendFieldState(SocketChannel channel) throws IOException {
        if (playersMap.containsKey(channel)) {
            Field field = playersMap.get(channel).getGame().getField();
            receiveCommand(new ResponseFieldCommand(field), channel);
        }
    }

    @Override
    public void applyInput(Direction direction, SocketChannel channel) throws IOException {
        PlayerRole role = playersMap.get(channel).getPlayerRole();
        Paddle paddle = null;
        boolean allowed = true;
        switch (role) {
            case FIRST:
                paddle = playersMap.get(channel).getGame().getPaddle(PlayerRole.FIRST);
                break;
            case SECOND:
                paddle = playersMap.get(channel).getGame().getPaddle(PlayerRole.SECOND);
                break;
            default:
                allowed = false;
        }
        if (allowed) {
            paddle.setAccelerationDirection(direction);
        }
        sendFieldState(channel);
    }

    @Override
    public void sendPlayerRole(SocketChannel channel) throws IOException {
        if (playersMap.containsKey(channel)) {
            receiveCommand(new ResponsePlayerRoleCommand(playersMap.get(channel).getPlayerRole()), channel);
        } else {
            receiveCommand(new ResponsePlayerRoleCommand(PlayerRole.DENIED), channel);
        }
    }

    @Override
    public void sendScore(SocketChannel channel) throws IOException {
        receiveCommand(new ResponseScoreCommand(playersMap.get(channel).getGame().getScore()), channel);
        System.out.println("Score sent");
    }

    private void removePlayer(SocketChannel channel) throws IOException {
        if (playersMap.containsKey(channel)) {
            PlayerRole role = playersMap.get(channel).getPlayerRole();
            playersMap.get(channel).getGame().setControlled(role, false);

            playersMap.get(channel).getGame().removePlayer(role);
            playersMap.remove(channel);
            receiveCommand(new ResponseMessageCommand("Exit success!"), channel);
        }
    }

    @Override
    public void connectToGame(SocketChannel channel, int index) throws IOException {
        if (!playersMap.containsKey(channel)) {
            PongGame game = (PongGame) PongGame.getActiveGamesMap().values().toArray()[index];
            PlayerRole role = game.addPlayer();
            playersMap.put(channel, new PlayerData(game, role));

            receiveCommand(new ResponsePlayerRoleCommand(role), channel);
            sendGameState(channel);
            sendScore(channel);
        }
        else {
            receiveCommand(new ResponseMessageCommand("You are playing a game!"), channel);
        }
    }

    @Override
    public void startNewGame(SocketChannel channel) throws IOException {
        PongGame game = new PongGame();
        PlayerRole role = game.addPlayer();
        playersMap.put(channel, new PlayerData(game, role));

        receiveCommand(new ResponsePlayerRoleCommand(role), channel);
        sendGameState(channel);
        sendScore(channel);
    }

    @Override
    public void confirm(SocketChannel channel) {
        if (playersMap.containsKey(channel)) {
            //playersMap.get(channel).getGame().setGameState(GameState.CONFIRMATION);
            PlayerRole role = playersMap.get(channel).getPlayerRole();
            playersMap.get(channel).getGame().setControlled(role, true);
        }

        if (playersMap.get(channel).getGame().isControlled(PlayerRole.FIRST) && playersMap.get(channel).getGame().isControlled(PlayerRole.SECOND)) {
            start(playersMap.get(channel).getGame());
        }
    }

    private void start(PongGame game) {
        game.start();
    }

    @Override
    public void sendGameState(SocketChannel channel) throws IOException {
        if (playersMap.containsKey(channel))
            receiveCommand(new ResponseGameStateCommand(playersMap.get(channel).getGame().getGameState()), channel);
    }

    @Override
    public void sendGameList(SocketChannel marker) throws IOException {
        ListMultimap<GameState, PongGame> map = PongGame.getActiveGamesMap();
        PongGame[] games = new PongGame[map.size()];
        games = map.values().toArray(games);
        receiveCommand(new ResponseGameListCommand(games), marker);
    }

    @Override
    public void sendGameUpdate(SocketChannel marker) throws IOException {
        receiveCommand(new ResponseEmptyCommand(), marker);
    }
}
