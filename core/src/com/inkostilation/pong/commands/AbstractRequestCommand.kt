package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.IEngine

abstract class AbstractRequestCommand<E: IEngine>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    abstract fun getEngineType(): Class<out IEngine>
}