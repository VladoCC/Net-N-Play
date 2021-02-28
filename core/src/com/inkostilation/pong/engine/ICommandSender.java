package com.inkostilation.pong.engine;

import com.inkostilation.pong.commands.AbstractResponseCommand;

import java.util.List;

public interface ICommandSender {

    boolean hasCommands();

    void getCommands(List<AbstractResponseCommand> pool);
}
