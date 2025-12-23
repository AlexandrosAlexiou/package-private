rootProject.name = "kotlin-package-private"

include(
  ":package-private-annotations",
  ":package-private-compiler-plugin",
  ":package-private-gradle-plugin",
  ":integration-tests",
  ":examples:gradle",
)

project(":examples:gradle").name = "example-gradle"
