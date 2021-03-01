package com.inkostilation.pong.commands

import com.inkostilation.pong.exceptions.NoEngineException
import java.io.IOException
import kotlin.jvm.Throws

interface ICommand<I, O> {
    @Throws(IOException::class, NoEngineException::class)
    fun execute(input: I): O
}