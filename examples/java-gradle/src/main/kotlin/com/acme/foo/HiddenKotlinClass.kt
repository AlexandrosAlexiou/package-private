package com.acme.foo

import dev.packageprivate.PackagePrivate

@PackagePrivate
class HiddenKotlinClass {
  fun helper(): String = "package-private from Kotlin"
}
