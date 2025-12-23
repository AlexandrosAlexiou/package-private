package com.acme.packageprivate.compiler

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertContains

@OptIn(ExperimentalCompilerApi::class)
class PackagePrivateCheckerTest {

    private val annotationSource = SourceFile.kotlin(
        "PackagePrivate.kt",
        """
        package com.acme.packageprivate

        @Target(
            AnnotationTarget.CLASS,
            AnnotationTarget.FUNCTION,
            AnnotationTarget.PROPERTY,
            AnnotationTarget.CONSTRUCTOR
        )
        @Retention(AnnotationRetention.BINARY)
        annotation class PackagePrivate(val scope: String = "")
        """
    )

    @Test
    fun `same package access is allowed`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Internal.kt",
                """
                package com.example.internal

                import com.acme.packageprivate.PackagePrivate

                @PackagePrivate
                class Internal {
                    @PackagePrivate
                    fun secret(): String = "secret"
                }
                """
            ),
            SourceFile.kotlin(
                "SamePackage.kt",
                """
                package com.example.internal

                fun useSamePackage(): String = Internal().secret()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `different package access to class is forbidden`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Internal.kt",
                """
                package com.example.internal

                import com.acme.packageprivate.PackagePrivate

                @PackagePrivate
                class Internal
                """
            ),
            SourceFile.kotlin(
                "OtherPackage.kt",
                """
                package com.example.other

                import com.example.internal.Internal

                fun useOtherPackage() = Internal()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertContains(result.messages, "Cannot access 'com.example.internal.Internal'")
        assertContains(result.messages, "package-private in 'com.example.internal'")
    }

    @Test
    fun `different package access to function is forbidden`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Internal.kt",
                """
                package com.example.internal

                import com.acme.packageprivate.PackagePrivate

                class Helper {
                    @PackagePrivate
                    fun secret(): String = "secret"
                }
                """
            ),
            SourceFile.kotlin(
                "OtherPackage.kt",
                """
                package com.example.other

                import com.example.internal.Helper

                fun useOtherPackage() = Helper().secret()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertContains(result.messages, "Cannot access")
        assertContains(result.messages, "package-private")
    }

    @Test
    fun `scope override allows access from specified package`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Generated.kt",
                """
                package com.example.generated

                import com.acme.packageprivate.PackagePrivate

                @PackagePrivate(scope = "com.example.api")
                class GeneratedHelper {
                    fun help(): String = "help"
                }
                """
            ),
            SourceFile.kotlin(
                "Api.kt",
                """
                package com.example.api

                import com.example.generated.GeneratedHelper

                fun useGenerated() = GeneratedHelper().help()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }

    @Test
    fun `scope override denies access from other packages`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Generated.kt",
                """
                package com.example.generated

                import com.acme.packageprivate.PackagePrivate

                @PackagePrivate(scope = "com.example.api")
                class GeneratedHelper
                """
            ),
            SourceFile.kotlin(
                "Other.kt",
                """
                package com.example.other

                import com.example.generated.GeneratedHelper

                fun useGenerated() = GeneratedHelper()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertContains(result.messages, "package-private in 'com.example.api'")
    }

    @Test
    fun `package-private constructor is enforced`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Internal.kt",
                """
                package com.example.internal

                import com.acme.packageprivate.PackagePrivate

                class Restricted @PackagePrivate constructor()
                """
            ),
            SourceFile.kotlin(
                "OtherPackage.kt",
                """
                package com.example.other

                import com.example.internal.Restricted

                fun create() = Restricted()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertContains(result.messages, "package-private")
    }

    @Test
    fun `top-level package-private function is enforced`() {
        val result = compile(
            annotationSource,
            SourceFile.kotlin(
                "Internal.kt",
                """
                package com.example.internal

                import com.acme.packageprivate.PackagePrivate

                @PackagePrivate
                fun internalUtil(): Int = 42
                """
            ),
            SourceFile.kotlin(
                "OtherPackage.kt",
                """
                package com.example.other

                import com.example.internal.internalUtil

                fun use() = internalUtil()
                """
            )
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, result.exitCode)
        assertContains(result.messages, "package-private")
    }

    private fun compile(vararg sourceFiles: SourceFile): JvmCompilationResult {
        return KotlinCompilation().apply {
            sources = sourceFiles.toList()
            compilerPluginRegistrars = listOf(PackagePrivateComponentRegistrar())
            inheritClassPath = true
            messageOutputStream = System.out
            languageVersion = "2.0"
        }.compile()
    }
}
