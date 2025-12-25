package com.example.internal

import dev.packageprivate.PackagePrivate

@PackagePrivate
class KotlinInternal {
    @PackagePrivate fun secret(): String = "secret"

    fun publicMethod(): String = "public"
}
