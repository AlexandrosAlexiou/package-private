plugins {
  kotlin("jvm")
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  testImplementation("org.jetbrains.kotlin:kotlin-test:2.3.0")
  testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.test {
  useJUnitPlatform()
  dependsOn(":package-private-annotations:publishToMavenLocal")
  dependsOn(":package-private-compiler-plugin:publishToMavenLocal")
  dependsOn(":package-private-analyzer-core:publishToMavenLocal")
  dependsOn(":package-private-analyzer-gradle-plugin:publishToMavenLocal")
  dependsOn(":package-private-analyzer-maven-plugin:publishToMavenLocal")
}
