package com.acme.foo;

// This Java class successfully accesses the public Kotlin class
public class JavaAccessPublic {
  public String usePublicKotlin() {
    PublicKotlinClass pub = new PublicKotlinClass();
    return pub.publicMethod();
  }
}
