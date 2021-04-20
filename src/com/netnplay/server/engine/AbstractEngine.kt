package com.netnplay.server.engine

import com.netnplay.commands.AbstractRequestCommand
import com.netnplay.commands.AbstractResponseCommand
import com.netnplay.server.network.Redirect

/**
 * Standard abstraction for [IEngine] that implements general functionality
 * responsible for execution of commands and interactions with [Redirect] object.
 */
abstract class AbstractEngine: IEngine {
    private var redirect = Redirect()

    override fun getRedirect() = redirect

    override fun process(command: AbstractRequestCommand<AbstractEngine>): Array<AbstractResponseCommand<*>> {
        redirect.reset()
        prepare()
        return command.execute(this)
    }

    /**
     * Simple utility method for resetting the engine and cleaning it
     * before executing the command.
     *
     * Purpose behind this method is to make sure that engines is ready
     * for the next command and there won't be any side-effects of
     * the last commands execution.
     *
     * This is one of the data sanitization measures that allows to
     * protect system from attacks.
     */
    abstract fun prepare()

    /**
     * Shortener for interacting with redirect from the command.
     */
    final fun redirect(engine: Class<out AbstractEngine>) {
        redirect.redirect(engine)
    }
}