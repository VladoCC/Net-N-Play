package com.inkostilation.pong.commands.response;

import com.inkostilation.pong.commands.AbstractResponseCommand;

public class ResponseMessageCommand extends AbstractResponseCommand {

    private String text;

    public ResponseMessageCommand(String text) {
        this.text = text;
    }

    @Override
    public void execute() {
        System.out.println(text);
    }
}
