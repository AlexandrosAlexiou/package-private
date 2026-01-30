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

// Note: To use the analyzer task in your own project, apply the gradle plugin:
//   plugins {
//     id("dev.packageprivate.package-private") version "1.2.0"
//   }
// Then run: ./gradlew analyzePackagePrivateCandidates
//
// The example source files in this module demonstrate:
// - InternalHelper.kt: Public class only used within package (CANDIDATE)
// - InternalObject.kt: Object only used within package (CANDIDATE)
// - utilityFunction: Internal function only used within package (CANDIDATE)
// - InternalService: Public class used across packages (NOT a candidate)
// - TypeUsageExample.kt: Demonstrates type reference detection
// - StarImportExample.kt: Demonstrates star import handling
// - QualifiedRefExample.kt: Demonstrates qualified reference handling
