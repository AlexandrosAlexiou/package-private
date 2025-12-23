package com.acme.packageprivate

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import kotlin.test.assertTrue
import kotlin.test.assertContains

class MavenIntegrationTest {

    @TempDir
    lateinit var tempDir: File

    @Test
    fun `maven build succeeds with same package access`() {
        copyResourceProject("maven-project", tempDir)
        // Remove the violating file for this test
        File(tempDir, "src/main/kotlin/com/example/api").deleteRecursively()

        val result = runMaven(tempDir, "compile")
        assertTrue(result.exitCode == 0, "Build should succeed: ${result.output}")
        assertContains(result.output, "Applied plugin: 'package-private'")
    }

    @Test
    fun `maven build fails with cross-package access`() {
        copyResourceProject("maven-project", tempDir)

        val result = runMaven(tempDir, "compile")
        assertTrue(result.exitCode != 0, "Build should fail")
        assertContains(result.output, "package-private")
    }

    private fun copyResourceProject(name: String, targetDir: File) {
        val resourceDir = File(javaClass.classLoader.getResource(name)!!.toURI())
        resourceDir.copyRecursively(targetDir, overwrite = true)
    }

    private fun runMaven(dir: File, vararg args: String): ProcessResult {
        val process = ProcessBuilder("mvn", *args)
            .directory(dir)
            .redirectErrorStream(true)
            .start()

        val output = process.inputStream.bufferedReader().readText()
        val exitCode = process.waitFor()
        return ProcessResult(exitCode, output)
    }

    data class ProcessResult(val exitCode: Int, val output: String)
}
