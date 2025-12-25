plugins {
  kotlin("jvm")
  id("java-gradle-plugin")
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin-api:2.0.21")
}

gradlePlugin {
  plugins {
    create("packagePrivate") {
      id = "com.acme.package-private"
      implementationClass = "dev.packageprivate.gradle.PackagePrivateGradlePlugin"
    }
  }
}
