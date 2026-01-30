package com.acme.internal

/**
 * Property candidate - only used within this package.
 */
val internalConfig: String = "config-value"

/**
 * Extension function candidate - only used within this package.
 */
fun String.toInternalFormat(): String = "[$this]"

/**
 * Extension property candidate - only used within this package.
 */
val String.internalLength: Int
    get() = this.length * 2
