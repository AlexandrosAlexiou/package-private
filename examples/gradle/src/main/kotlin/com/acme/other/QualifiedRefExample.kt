package com.acme.other

/**
 * This file uses fully qualified references (no import).
 * The analyzer correctly detects these usages.
 */
class QualifiedRefExample {
    // Qualified reference without import
    private val api = com.acme.api.PublicApi()
    
    // Also works for function calls
    fun run(): String = com.acme.api.PublicApi().execute()
}
