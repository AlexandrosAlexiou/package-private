plugins {
  kotlin("jvm")
  java
}

kotlin {
  jvmToolchain(21)
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

dependencies {
  implementation(project(":package-private-annotations"))
  kotlinCompilerPluginClasspath(project(":package-private-compiler-plugin"))
}
