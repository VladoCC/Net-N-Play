package com.inkostilation.pong.execution;

import com.inkostilation.pong.commands.ICommand;

public interface IExecutor<C extends ICommand> {

    void call(C command);
}
