# Package Private Analyzer Maven Plugin

This Maven plugin analyzes Kotlin code to find candidates for the `@PackagePrivate` annotation.

## Building

**This module is built with Gradle** (like all other modules in this project).

```bash
# From the project root
./gradlew :package-private-analyzer-maven-plugin:build
./gradlew :package-private-analyzer-maven-plugin:publishToMavenLocal
```

Or build all modules:

```bash
./gradlew build
./gradlew publishToMavenLocal
```

The Gradle build automatically generates the Maven plugin descriptor at build time.

## Usage

Add the plugin to your Maven project's `pom.xml`:

```xml
<plugin>
    <groupId>dev.packageprivate</groupId>
    <artifactId>package-private-analyzer-maven-plugin</artifactId>
    <version>1.2.0</version>
    <executions>
        <execution>
            <goals>
                <goal>analyze</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

Run the analyzer:

```bash
mvn dev.packageprivate:package-private-analyzer-maven-plugin:1.2.0:analyze
```

Or if configured in the build lifecycle:

```bash
mvn verify
```

## Configuration

Available parameters:

- `includePublic` (default: `true`) - Include public declarations in candidate analysis
- `includeInternal` (default: `true`) - Include internal declarations in candidate analysis  
- `outputFile` (default: `${project.build.directory}/reports/package-private-candidates.txt`) - Output file path
- `sourceDirectories` - Custom source directories (defaults to `src/main/kotlin` and `src/main/java`)

Example configuration:

```xml
<plugin>
    <groupId>dev.packageprivate</groupId>
    <artifactId>package-private-analyzer-maven-plugin</artifactId>
    <version>1.2.0</version>
    <configuration>
        <includePublic>true</includePublic>
        <includeInternal>false</includeInternal>
        <outputFile>${project.basedir}/analysis-report.txt</outputFile>
    </configuration>
</plugin>
```
