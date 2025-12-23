# kotlin-package-private v0.1.0

Initial release of the Kotlin package-private visibility compiler plugin.

## Features

- `@PackagePrivate` annotation for classes, functions, properties, and constructors
- Compile-time enforcement via K2 FIR checker
- Optional `scope` parameter to override package restriction
- Gradle and Maven support

## Installation

### Gradle

```kotlin
dependencies {
    implementation("com.acme:package-private-annotations:0.1.0")
    kotlinCompilerPluginClasspath("com.acme:package-private-compiler-plugin:0.1.0")
}
```

### Maven

```xml
<dependency>
    <groupId>com.acme</groupId>
    <artifactId>package-private-annotations</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Requirements

- Kotlin 2.0.21+
- JDK 21+

## Documentation

See [README](https://github.com/AlexandrosAlexiou/kotlin-package-private#readme) for full usage instructions.
