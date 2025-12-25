plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("dev.packageprivate:package-private-annotations:1.0.0")
    kotlinCompilerPluginClasspath("dev.packageprivate:package-private-compiler-plugin:1.0.0")
}
