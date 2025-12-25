package com.acme.foo;

public class SuccessfulAccess {
  public static void main(String[] args) {
    // This should work - accessing public Kotlin class from Java
    PublicKotlinClass pub = new PublicKotlinClass();
    System.out.println("Accessing public Kotlin class: " + pub.publicMethod());
    
    // This should work - accessing package-private class within same package
    HiddenKotlinClass hidden = new HiddenKotlinClass();
    System.out.println("Same package access: " + hidden.helper());
  }
}
