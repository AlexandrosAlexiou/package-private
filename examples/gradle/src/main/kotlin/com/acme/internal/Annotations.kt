package com.acme.internal

/**
 * Annotation candidate - only used within this package.
 */
annotation class InternalMarker

/**
 * Class using the annotation.
 */
@InternalMarker
class MarkedService {
    @InternalMarker
    fun markedMethod(): Int = 42
}
