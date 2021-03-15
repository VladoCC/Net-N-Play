package com.inkostilation.pong.server.engine

import com.inkostilation.pong.commands.AbstractRequestCommand
import com.inkostilation.pong.commands.AbstractResponseCommand
import com.inkostilation.pong.server.network.Redirect

abstract class AbstractEngine: IEngine {
    private var redirect = Redirect()

    override fun getRedirect() = redirect

    override fun process(command: AbstractRequestCommand<AbstractEngine>): Array<AbstractResponseCommand<*>> {
        redirect.reset()
        prepare()
        return command.execute(this)
    }

    abstract fun prepare()

    final fun redirect(engine: Class<out AbstractEngine>) {
        redirect.redirect(engine)
    }
}