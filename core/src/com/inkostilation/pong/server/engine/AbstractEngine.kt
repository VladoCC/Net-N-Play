package com.inkostilation.pong.server.engine

import com.inkostilation.pong.server.network.Redirect

abstract class AbstractEngine<M>: IEngine<M> {
    private lateinit var redirect: Redirect<M>
    final override fun start(redirect: Redirect<M>) {
        this.redirect = redirect
    }

    protected fun redirect() {
        redirect.redirect()
    }
}