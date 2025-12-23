package com.example.api

import com.example.internal.Helper

// This violates package-private - should cause compilation error
fun createHelper() = Helper()
