package com.inkostilation.pong.commands;

import com.inkostilation.pong.engine.IEngine;
import com.inkostilation.pong.exceptions.NoEngineException;

import java.io.IOException;

public abstract class AbstractRequestCommand<E extends IEngine<M>, M> implements ICommand {

    protected E engine;
    private M marker;

    public void setEngine(E engine) {
        this.engine = engine;
    }

    public void setMarker(M marker) {
        this.marker = marker;
    }

    protected E getEngine() {
        return engine;
    }

    public M getMarker() {
        return marker;
    }

    @Override
    public void execute() throws NoEngineException, IOException {
        if (engine == null) {
            throw new NoEngineException();
        }
        execute(engine);
    }

    protected abstract void execute(E engine) throws IOException;
}
