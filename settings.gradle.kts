rootProject.name = "package-private"

include(
  ":package-private-annotations",
  ":package-private-compiler-plugin",
  ":package-private-gradle-plugin",
  ":package-private-analyzer-core",
  ":package-private-analyzer-gradle-plugin",
  ":package-private-analyzer-maven-plugin",
  ":integration-tests",
  ":examples:java-gradle",
)

project(":examples:java-gradle").name = "example-java-gradle"
