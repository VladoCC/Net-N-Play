package com.inkostilation.pong.commands

import com.inkostilation.pong.server.engine.IEngine
import kotlin.jvm.Throws

abstract class AbstractRequestCommand<E : IEngine<M>, M>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    abstract fun getEngineType(): Class<out IEngine<*>>
}