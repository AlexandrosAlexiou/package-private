rootProject.name = "package-private"

include(
  ":package-private-annotations",
  ":package-private-compiler-plugin",
  ":package-private-gradle-plugin",
  ":integration-tests",
  ":examples:java-gradle",
  ":examples:multiplatform-js",
)

project(":examples:java-gradle").name = "example-java-gradle"
project(":examples:multiplatform-js").name = "example-multiplatform-js"

// example-gradle is a standalone build that uses the plugin via includeBuild
// Run it with: cd examples/gradle && ./gradlew analyzePackagePrivateCandidates
