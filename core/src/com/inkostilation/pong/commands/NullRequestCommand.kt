package com.inkostilation.pong.commands

import com.inkostilation.pong.engine.IEngine
import java.io.IOException

class NullRequestCommand : AbstractRequestCommand<Any?, Any?>() {
    @Throws(IOException::class)
    protected override fun execute(engine: IEngine<*>?) {
    }
}