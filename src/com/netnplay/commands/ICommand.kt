package com.netnplay.commands

/**
 * Interface based on the command pattern and designed to be serializable
 * and reproducible. Used to send data and instructions through the network.
 *
 * Generic parameter [I] stands for input type of the commands [execute] method.
 * Generic parameter [O] stands for output (return) type of the commands
 * [execute] method.
 */
interface ICommand<I, O> {
    /**
     * Main method of command. It's designed to be an entry point
     * to all of the other command code.
     */
    fun execute(input: I): O
}