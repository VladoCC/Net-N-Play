package com.netnplay.commands

import com.netnplay.exceptions.NoEngineException
import java.io.IOException

interface ICommand<I, O> {
    @Throws(IOException::class, NoEngineException::class)
    fun execute(input: I): O
}