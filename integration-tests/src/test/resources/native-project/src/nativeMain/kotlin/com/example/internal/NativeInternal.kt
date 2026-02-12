package com.example.internal

import dev.packageprivate.PackagePrivate

@PackagePrivate
class NativeInternal {
    @PackagePrivate fun secret(): String = "secret"

    fun publicMethod(): String = "public"
}

@PackagePrivate fun internalHelper(): Int = 42
