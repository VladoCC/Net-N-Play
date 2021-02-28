package com.inkostilation.pong.commands.response;

import com.inkostilation.pong.commands.AbstractResponseCommand;

public class ResponseObjectCommand extends AbstractResponseCommand {

    private Object object;

    public ResponseObjectCommand(Object object) {
        this.object = object;
    }

    @Override
    public void execute() {
        System.out.println(object.toString());
    }
}