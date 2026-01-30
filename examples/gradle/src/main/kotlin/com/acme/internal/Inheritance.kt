package com.acme.internal

/**
 * Base class for inheritance example - only extended within this package.
 */
open class InternalBase {
    open fun doWork(): Int = 0
}

/**
 * Interface for inheritance example - only implemented within this package.
 */
interface InternalContract {
    fun execute(): String
}

// Implementation within same package
class InternalImpl : InternalBase(), InternalContract {
    override fun doWork(): Int = 42
    override fun execute(): String = "done"
}
