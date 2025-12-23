plugins {
  kotlin("jvm") version "2.0.21" apply false
}

group = "com.acme"
version = "0.1.0"

subprojects {
  repositories {
    mavenCentral()
  }

  // Apply maven-publish to library modules (not examples)
  if (!project.path.startsWith(":examples")) {
    apply(plugin = "maven-publish")

    afterEvaluate {
      configure<PublishingExtension> {
        publications {
          create<MavenPublication>("maven") {
            from(components.findByName("java") ?: components.findByName("kotlin"))
            groupId = rootProject.group.toString()
            artifactId = project.name
            version = rootProject.version.toString()
          }
        }
      }
    }
  }
}
