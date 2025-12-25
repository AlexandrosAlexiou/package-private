package dev.packageprivate.compiler

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

@OptIn(ExperimentalCompilerApi::class)
class PackagePrivateCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String = "com.acme.package-private"
    override val pluginOptions: Collection<AbstractCliOption> = emptyList()
}
