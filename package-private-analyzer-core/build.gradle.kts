plugins {
    kotlin("jvm")
    id("maven-publish")
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        // Opt-in to K1 API until K2-compatible standalone PSI parsing is available
        freeCompilerArgs.add("-opt-in=org.jetbrains.kotlin.K1Deprecation")
    }
}

dependencies {
    // For Kotlin PSI parsing in the analyzer
    // Note: This causes a KGP warning about compiler-embeddable in the classpath.
    // This is expected and acceptable for this plugin's functionality (PSI parsing).
    // See: https://kotl.in/gradle/internal-compiler-symbols
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:2.3.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test:2.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test { useJUnitPlatform() }

publishing { repositories { mavenLocal() } }
