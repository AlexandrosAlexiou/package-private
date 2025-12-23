package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.jvm.extensions.ClassGeneratorExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.platform.isJs
import org.jetbrains.kotlin.platform.isWasm
import org.jetbrains.kotlin.platform.jvm.isJvm
import org.jetbrains.kotlin.platform.konan.isNative

@OptIn(ExperimentalCompilerApi::class)
class PackagePrivateComponentRegistrar : CompilerPluginRegistrar() {
    override val supportsK2: Boolean = true

    override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
        // FIR checker for Kotlin compile-time errors (all platforms)
        FirExtensionRegistrarAdapter.registerExtension(PackagePrivateFirExtensionRegistrar())
        
        // JVM: Use ClassGeneratorExtension for true JVM package-private bytecode
        ClassGeneratorExtension.registerExtension(PackagePrivateClassGeneratorExtension())
        
        // Non-JVM platforms: Use IR transformation to set internal visibility
        // This provides module-level restriction for Native/JS/Wasm
        IrGenerationExtension.registerExtension(PackagePrivateIrGenerationExtension())
    }
}
