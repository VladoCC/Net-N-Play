package com.netnplay.commands

/**
 * Subtype of [ICommand] that represent server's response to clients requests.
 * It uses [Unit] as an output type for [execute] method.
 *
 * Generic parameter [I] let's any type to be used as an input type for this type of commands.
 */
abstract class AbstractResponseCommand<I> : ICommand<I, Unit>