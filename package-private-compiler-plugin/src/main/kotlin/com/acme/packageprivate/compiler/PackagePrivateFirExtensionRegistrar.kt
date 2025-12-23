package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

class PackagePrivateFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::PackagePrivateChecker
    }
}
