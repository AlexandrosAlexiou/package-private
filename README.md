<p align="center">
  <img src=".github/logo.svg" alt="kotlin-package-private logo" width="150">
</p>

<h1 align="center">kotlin-package-private</h1>

<p align="center">
A Kotlin compiler plugin that adds <b>package-private</b> visibility to Kotlin, similar to Java's default (package) visibility.
</p>

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)


## The Problem

Kotlin's visibility modifiers have a gap:

| Modifier | Scope | Too Broad? | Too Narrow? |
|----------|-------|------------|-------------|
| `private` | Single file | | Yes - Can't share across files in same package |
| `internal` | Entire module | Yes - Exposes to all packages | |
| `protected` | Subclasses | | Yes - Only inheritance |
| `public` | Everywhere | Yes - No encapsulation | |

**Missing: Package-private** - visible only within the same package, across multiple files.

## The Solution

```kotlin
package com.example.internal

@PackagePrivate
class Helper {  // Only accessible within com.example.internal
    @PackagePrivate
    fun secretMethod() = "hidden from other packages"
}
```

```kotlin
package com.example.api

import com.example.internal.Helper  // ❌ Compilation error!
// Cannot access 'Helper': it is package-private in 'com.example.internal'
```

## Who Is This For?

- **Library authors** - Hide implementation details without creating separate modules
- **Large codebases** - Enforce package boundaries within a single module
- **Clean Architecture** - Keep domain logic separate from infrastructure
- **Java developers** - Familiar visibility model when migrating to Kotlin
- **Multiplatform projects** - Works on JVM, Native, JS, Android, iOS

## Features

- `@PackagePrivate` annotation for classes, functions, properties, and constructors
- Compile-time enforcement - access violations are reported as compilation errors
- Optional `scope` parameter to override the package (useful for generated code)
- Works with both **Gradle** and **Maven**
- Supports Kotlin 2.0+ (K2 compiler)

## Installation

### Repository Setup

Add the GitHub Packages repository (requires authentication):

<details>
<summary><b>Gradle Setup</b></summary>

Add to `~/.gradle/gradle.properties`:
```properties
gpr.user=YOUR_GITHUB_USERNAME
gpr.key=YOUR_GITHUB_TOKEN
```

Create a token at https://github.com/settings/tokens with `read:packages` scope.
</details>

<details>
<summary><b>Maven Setup</b></summary>

Add to `~/.m2/settings.xml`:
```xml
<servers>
  <server>
    <id>github</id>
    <username>YOUR_GITHUB_USERNAME</username>
    <password>YOUR_GITHUB_TOKEN</password>
  </server>
</servers>
```
</details>

### Gradle (Kotlin DSL)

```kotlin
// build.gradle.kts
plugins {
    kotlin("jvm") version "2.0.21"
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/AlexandrosAlexiou/kotlin-package-private")
        credentials {
            username = project.findProperty("gpr.user") as String?
            password = project.findProperty("gpr.key") as String?
        }
    }
}

dependencies {
    implementation("com.acme:package-private-annotations:0.1.0")
    kotlinCompilerPluginClasspath("com.acme:package-private-compiler-plugin:0.1.0")
}
```

### Gradle (Groovy DSL)

```groovy
// build.gradle
plugins {
    id 'org.jetbrains.kotlin.jvm' version '2.0.21'
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/AlexandrosAlexiou/kotlin-package-private")
        credentials {
            username = project.findProperty("gpr.user")
            password = project.findProperty("gpr.key")
        }
    }
}

dependencies {
    implementation 'com.acme:package-private-annotations:0.1.0'
    kotlinCompilerPluginClasspath 'com.acme:package-private-compiler-plugin:0.1.0'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/AlexandrosAlexiou/kotlin-package-private</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.acme</groupId>
        <artifactId>package-private-annotations</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.jetbrains.kotlin</groupId>
            <artifactId>kotlin-maven-plugin</artifactId>
            <version>2.0.21</version>
            <executions>
                <execution>
                    <id>compile</id>
                    <goals>
                        <goal>compile</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <compilerPlugins>
                    <plugin>package-private</plugin>
                </compilerPlugins>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>com.acme</groupId>
                    <artifactId>package-private-compiler-plugin</artifactId>
                    <version>0.1.0</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

## Usage

### Basic Usage

```kotlin
// com/example/internal/Helper.kt
package com.example.internal

import com.acme.packageprivate.PackagePrivate

@PackagePrivate
class Helper {
    @PackagePrivate
    fun doSomething(): String = "internal work"
}

@PackagePrivate
fun utilityFunction(): Int = 42
```

```kotlin
// com/example/internal/Service.kt
package com.example.internal

// ✅ Same package - allowed
class Service {
    fun execute() = Helper().doSomething()
}
```

```kotlin
// com/example/api/PublicApi.kt
package com.example.api

import com.example.internal.Helper

// ❌ Compilation error:
// Cannot access 'com.example.internal.Helper': it is package-private in 'com.example.internal'
fun broken() = Helper()
```

### Annotation Targets

`@PackagePrivate` can be applied to:

| Target | Example |
|--------|---------|
| Class | `@PackagePrivate class Internal` |
| Function | `@PackagePrivate fun helper()` |
| Property | `@PackagePrivate val config: Config` |
| Constructor | `class Foo @PackagePrivate constructor()` |

### Scope Override

The `scope` parameter allows overriding the package scope. This is useful for:
- Generated code that ends up in a different package
- Refactored code where the original package should still have access

```kotlin
package com.example.generated

import com.acme.packageprivate.PackagePrivate

// Accessible from com.example.api instead of com.example.generated
@PackagePrivate(scope = "com.example.api")
class GeneratedHelper
```

## Platform Support

| Platform | Compile-time | Runtime | How It Works |
|----------|:------------:|:-------:|--------------|
| **JVM/Android** | Yes | Yes | Removes `ACC_PUBLIC` bytecode flag → true JVM package-private, Java blocked |
| **Native** (iOS, macOS, Linux) | Yes | Yes | Sets IR visibility to `internal` → name mangling prevents cross-module linking |
| **JS/Wasm** | Yes | No | FIR checker only - JS has no runtime visibility enforcement |

**Why not KSP?** KSP is for code generation - it can't report compilation errors. We need the FIR (Frontend IR) checker to block cross-package access at compile time.

### Limitations

- **Reflection (JVM)**: Can bypass with `setAccessible(true)`
- **JS/Wasm**: Raw JavaScript can access anything - compile-time checks only
- **Native C interop**: Exported C headers may still be accessible

## Project Structure

```
kotlin-package-private/
├── package-private-annotations/     # @PackagePrivate annotation (multiplatform)
├── package-private-compiler-plugin/ # K2 compiler plugin (FIR checker + bytecode transformer)
├── package-private-gradle-plugin/   # Gradle plugin wrapper
├── integration-tests/               # Integration tests for Gradle/Maven
└── examples/
    ├── gradle/                      # Gradle example
    └── maven/                       # Maven example
```

**Note:** Maven doesn't need a separate plugin - it discovers the compiler plugin via `components.xml` automatically.

## Requirements

- Kotlin 2.0.21+
- JDK 21+

## Building & Testing

```bash
./gradlew build          # Build all modules
./gradlew publishToMavenLocal  # Install locally for examples
```

Test examples:
```bash
# Gradle example
./gradlew :examples:example-gradle:build

# Maven example (after publishToMavenLocal)
cd examples/maven && mvn compile
```

## License

Apache License 2.0

## Contributing

Contributions welcome! Please open an issue or PR.
