plugins {
    kotlin("jvm") version "2.3.10"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("dev.packageprivate:package-private-annotations:1.3.2")
    kotlinCompilerPluginClasspath("dev.packageprivate:package-private-compiler-plugin:1.3.2")
}
