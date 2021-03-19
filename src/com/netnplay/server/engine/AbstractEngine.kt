package com.netnplay.server.engine

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.network.Redirect

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