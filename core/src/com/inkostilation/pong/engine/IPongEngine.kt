package com.inkostilation.pong.engine

import java.io.IOException

interface IPongEngine<M> : IEngine<M> {
    @Throws(IOException::class)
    fun sendFieldState(marker: M)

    @Throws(IOException::class)
    fun sendPlayerRole(marker: M)

    @Throws(IOException::class)
    fun sendScore(marker: M)

    @Throws(IOException::class)
    fun applyInput(direction: Direction?, marker: M)

    @Throws(IOException::class)
    fun startNewGame(marker: M)

    @Throws(IOException::class)
    fun connectToGame(marker: M, index: Int)

    fun confirm(Marker: M)
    @Throws(IOException::class)
    fun sendGameState(marker: M)

    @Throws(IOException::class)
    fun sendGameList(marker: M)

    @Throws(IOException::class)
    fun sendGameUpdate(marker: M)
}