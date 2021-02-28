package com.inkostilation.pong.commands;

import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public interface ICommand {

    void execute() throws IOException, NoEngineException;
}
