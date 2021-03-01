package com.inkostilation.pong.execution

import com.inkostilation.pong.commands.ICommand

interface IExecutor<C : ICommand?> {
    fun call(command: C)
}