package com.inkostilation.pong.server.network

import com.inkostilation.pong.engine.IEngine

class Redirect<M>(private val router: ICommandRouter<M>) {
    fun redirect(marker: M, engine: Class<out IEngine<M>>) = router.reroute(marker, engine)
}