package com.inkostilation.pong.commands.response;


import com.inkostilation.pong.commands.AbstractResponseCommand;
import com.inkostilation.pong.engine.Field;

public class ResponseFieldCommand extends AbstractResponseCommand {

    private Field field;

    public ResponseFieldCommand(Field field) {
        this.field = field;
    }

    @Override
    public void execute()
    {
        getNotifier().notifyObservers(field);
        getNotifier().notifyObservers(field.getPaddle1());
        getNotifier().notifyObservers(field.getPaddle2());
        getNotifier().notifyObservers(field.getBall());
    }
}
