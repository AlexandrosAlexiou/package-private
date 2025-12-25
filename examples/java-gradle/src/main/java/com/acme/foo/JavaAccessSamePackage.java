package com.acme.foo;

// This Java class successfully accesses package-private Kotlin class in same package
public class JavaAccessSamePackage {
  public String useHiddenKotlin() {
    // This works - same package can access @PackagePrivate
    HiddenKotlinClass hidden = new HiddenKotlinClass();
    return hidden.helper();
  }
}
