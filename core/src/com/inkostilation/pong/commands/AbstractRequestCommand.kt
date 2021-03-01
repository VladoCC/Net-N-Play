package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IEngine
import java.io.IOException
import kotlin.jvm.Throws
import kotlin.reflect.KClass

abstract class AbstractRequestCommand<E : IEngine<M>?, M>: ICommand<E, Array<AbstractResponseCommand>> {

    var marker: M? = null
    val engineType: KClass<IEngine<*>>
        get() = IEngine::class
}