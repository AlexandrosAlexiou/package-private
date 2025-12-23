package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.diagnostics.SourceElementPositioningStrategies
import org.jetbrains.kotlin.diagnostics.error2
import org.jetbrains.kotlin.diagnostics.rendering.BaseDiagnosticRendererFactory
import org.jetbrains.kotlin.diagnostics.rendering.CommonRenderers
import org.jetbrains.kotlin.diagnostics.rendering.RootDiagnosticRendererFactory
import org.jetbrains.kotlin.psi.KtElement

object PackagePrivateErrors {
    val PACKAGE_PRIVATE_ACCESS by error2<KtElement, String, String>(
        SourceElementPositioningStrategies.DEFAULT
    )

    init {
        RootDiagnosticRendererFactory.registerFactory(PackagePrivateErrorMessages)
    }
}

object PackagePrivateErrorMessages : BaseDiagnosticRendererFactory() {
    override val MAP = org.jetbrains.kotlin.diagnostics.KtDiagnosticFactoryToRendererMap("PackagePrivate").also { map ->
        map.put(
            PackagePrivateErrors.PACKAGE_PRIVATE_ACCESS,
            "Cannot access ''{0}'': it is package-private in ''{1}''",
            CommonRenderers.STRING,
            CommonRenderers.STRING
        )
    }
}
