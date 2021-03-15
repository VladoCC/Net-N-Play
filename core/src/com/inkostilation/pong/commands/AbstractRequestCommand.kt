package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.AbstractEngine

abstract class AbstractRequestCommand<E: AbstractEngine>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    abstract fun getEngineType(): Class<out AbstractEngine>
}