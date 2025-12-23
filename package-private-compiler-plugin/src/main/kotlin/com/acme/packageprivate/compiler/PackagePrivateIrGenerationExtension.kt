package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.FqName

/**
 * IR transformation extension for Kotlin/Native and other non-JVM platforms.
 * 
 * This extension changes the visibility of @PackagePrivate declarations to `internal`
 * at the IR level. While this doesn't provide true package-private semantics,
 * it restricts access at the module level for non-JVM platforms.
 * 
 * For JVM, the ClassGeneratorExtension provides true JVM package-private visibility.
 * For Native/JS/Wasm, this IR transformation provides module-level restriction.
 */
class PackagePrivateIrGenerationExtension : IrGenerationExtension {
    
    companion object {
        private val PACKAGE_PRIVATE_FQ_NAME = FqName("com.acme.packageprivate.PackagePrivate")
    }

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.transformChildrenVoid(PackagePrivateVisibilityTransformer())
    }

    private inner class PackagePrivateVisibilityTransformer : IrElementTransformerVoid() {
        
        override fun visitClass(declaration: IrClass): IrStatement {
            if (declaration.hasAnnotation(PACKAGE_PRIVATE_FQ_NAME)) {
                declaration.visibility = DescriptorVisibilities.INTERNAL
            }
            return super.visitClass(declaration)
        }

        override fun visitSimpleFunction(declaration: IrSimpleFunction): IrStatement {
            if (declaration.hasAnnotation(PACKAGE_PRIVATE_FQ_NAME)) {
                declaration.visibility = DescriptorVisibilities.INTERNAL
            }
            return super.visitSimpleFunction(declaration)
        }

        override fun visitProperty(declaration: IrProperty): IrStatement {
            if (declaration.hasAnnotation(PACKAGE_PRIVATE_FQ_NAME)) {
                declaration.visibility = DescriptorVisibilities.INTERNAL
                // Also update getter/setter visibility
                declaration.getter?.visibility = DescriptorVisibilities.INTERNAL
                declaration.setter?.visibility = DescriptorVisibilities.INTERNAL
            }
            return super.visitProperty(declaration)
        }

        override fun visitConstructor(declaration: IrConstructor): IrStatement {
            if (declaration.hasAnnotation(PACKAGE_PRIVATE_FQ_NAME)) {
                declaration.visibility = DescriptorVisibilities.INTERNAL
            }
            return super.visitConstructor(declaration)
        }
    }
}
