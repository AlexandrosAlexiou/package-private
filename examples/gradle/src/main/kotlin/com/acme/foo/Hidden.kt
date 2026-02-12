package com.acme.foo

import dev.packageprivate.PackagePrivate

@PackagePrivate
class Hidden {
    @PackagePrivate fun helper(): String = "ok"
}
