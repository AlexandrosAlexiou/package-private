package com.acme.internal

/**
 * Nested class example - Container.Nested is only used within this package.
 */
class Container {
    class Nested {
        fun compute(): Int = 42
    }
    
    // Inner class (has reference to outer)
    inner class Inner {
        fun getOuter(): Container = this@Container
    }
}
