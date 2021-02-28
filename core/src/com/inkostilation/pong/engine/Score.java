package com.inkostilation.pong.engine;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.commands.response.ResponseScoreCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Score implements ICommandSender {

    private final int maxScoreValue = 5;
    private Map<PlayerRole, Integer> points = new HashMap<>();
    private boolean changed = false;

    public Score() {
        points.put(PlayerRole.FIRST, 0);
        points.put(PlayerRole.SECOND, 0);
    }

    public int getPlayerScore(PlayerRole role) {
        return points.get(role);
    }

    public int getMaxScoreValue() {
        return maxScoreValue;
    }

    public void addPlayerScore(PlayerRole role, int add)
    {
        int val = points.get(role);
        points.replace(role, val + add);

        if (add != 0) {
            changed = true;
        }
    }

    public long getMaxValueCount() {
        return points.values()
                .stream()
                .filter(o -> o >= getMaxScoreValue())
                .count();
    }

    public List<PlayerRole> getMaxedPlayers() {
        return points.entrySet()
                .stream()
                .filter(o -> o.getValue() >= getMaxScoreValue())
                .map(o -> o.getKey())
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasCommands() {
        return changed;
    }

    @Override
    public void getCommands(List<AbstractResponseCommand> pool) {
        if (changed) {
            pool.add(new ResponseScoreCommand(this));
            changed = false;
        }
    }
}
