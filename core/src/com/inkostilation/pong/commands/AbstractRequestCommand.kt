package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IEngine
import java.io.IOException
import kotlin.jvm.Throws
import kotlin.reflect.KClass

abstract class AbstractRequestCommand<E : IEngine<M>, M>: ICommand<E, Array<AbstractResponseCommand<*>>> {
    abstract fun getEngineType(): Class<out IEngine<*>>
}