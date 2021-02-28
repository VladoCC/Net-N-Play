package com.inkostilation.pong.commands.response;

import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public class ResponseEmptyCommand extends AbstractResponseCommand {
    @Override
    public void execute() throws IOException, NoEngineException {

    }
}
