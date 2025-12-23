package com.example.internal

import com.acme.packageprivate.PackagePrivate

@PackagePrivate
class Helper {
    @PackagePrivate
    fun work() = "done"
}
