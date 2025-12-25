package com.example.internal

import dev.packageprivate.PackagePrivate

@PackagePrivate
class Helper {
    @PackagePrivate
    fun work() = "done"
}
