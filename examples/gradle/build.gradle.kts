plugins {
  kotlin("jvm")
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  implementation(project(":package-private-annotations"))
  kotlinCompilerPluginClasspath(project(":package-private-compiler-plugin"))
}
