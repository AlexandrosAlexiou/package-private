package com.acme.foo

import com.acme.packageprivate.PackagePrivate

@PackagePrivate
class Hidden {
  @PackagePrivate
  fun helper(): String = "ok"
}
