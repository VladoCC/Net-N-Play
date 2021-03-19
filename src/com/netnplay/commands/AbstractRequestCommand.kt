package com.netnplay.commands

import com.netnplay.server.engine.AbstractEngine

abstract class AbstractRequestCommand<E: AbstractEngine>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    abstract fun getEngineType(): Class<out AbstractEngine>
}