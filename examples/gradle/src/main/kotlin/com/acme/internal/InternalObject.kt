package com.acme.internal

/**
 * This object is a good candidate for @PackagePrivate!
 * It's only used within this package.
 */
object InternalObject {
    fun getInstance(): String = "singleton"
}
