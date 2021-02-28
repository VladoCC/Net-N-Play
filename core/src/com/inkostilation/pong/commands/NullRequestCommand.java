package com.inkostilation.pong.commands;

import com.inkostilation.pong.engine.IEngine;

import java.io.IOException;

public class NullRequestCommand extends AbstractRequestCommand {
    @Override
    protected void execute(IEngine engine) throws IOException {

    }
}
