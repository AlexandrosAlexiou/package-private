package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.backend.jvm.extensions.ClassGeneratorExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter

@OptIn(ExperimentalCompilerApi::class)
class PackagePrivateComponentRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        // FIR checker for Kotlin compile-time errors (all platforms)
        FirExtensionRegistrarAdapter.registerExtension(PackagePrivateFirExtensionRegistrar())
        
        // JVM: Use ClassGeneratorExtension for true JVM package-private bytecode
        ClassGeneratorExtension.registerExtension(PackagePrivateClassGeneratorExtension())
    }
}
