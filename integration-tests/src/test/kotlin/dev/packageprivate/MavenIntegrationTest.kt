package dev.packageprivate

import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class MavenIntegrationTest {

    @TempDir lateinit var tempDir: File

    @Test
    fun `maven build succeeds with same package access`() {
        copyResourceProject("maven-project", tempDir)
        // Remove the violating file for this test
        File(tempDir, "src/main/kotlin/com/example/api").deleteRecursively()

        val result = runMaven(tempDir, "compile")
        assertEquals(result.exitCode, 0, "Build should succeed: ${result.output}")
        assertContains(result.output, "Applied plugin: 'package-private'")
    }

    @Test
    fun `maven build fails with cross-package access`() {
        copyResourceProject("maven-project", tempDir)

        val result = runMaven(tempDir, "compile")
        assertTrue(result.exitCode != 0, "Build should fail")
        assertContains(result.output, "package-private")
    }

    @Test
    fun `maven java cannot access package-private kotlin class`() {
        copyResourceProject("maven-java-interop-project", tempDir)

        val result = runMaven(tempDir, "compile")
        // Java should fail to compile because KotlinInternal is now package-private in bytecode
        assertTrue(
            result.exitCode != 0,
            "Build should fail when Java accesses package-private Kotlin: ${result.output}",
        )
    }

    @Test
    fun `maven java can access public members of class with package-private members`() {
        copyResourceProject("maven-java-interop-positive-project", tempDir)

        val result = runMaven(tempDir, "compile")
        // Java should successfully access public members even when class has some @PackagePrivate
        // members
        assertEquals(
            result.exitCode,
            0,
            "Build should succeed when accessing public members: ${result.output}",
        )
    }

    @Test
    fun `maven java in same package can access package-private kotlin`() {
        copyResourceProject("maven-java-same-package-project", tempDir)

        val result = runMaven(tempDir, "compile")
        // Java in same package should be allowed to access package-private Kotlin
        assertEquals(
            result.exitCode,
            0,
            "Build should succeed for same package access: ${result.output}",
        )
    }

    private fun copyResourceProject(name: String, targetDir: File) {
        val resourceDir = File(javaClass.classLoader.getResource(name)!!.toURI())
        resourceDir.copyRecursively(targetDir, overwrite = true)
    }

    private fun runMaven(dir: File, vararg args: String): ProcessResult {
        val process =
            ProcessBuilder("mvn", "clean", *args).directory(dir).redirectErrorStream(true).start()

        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        return ProcessResult(exitCode, output)
    }

    data class ProcessResult(val exitCode: Int, val output: String)
}
