package com.acme.packageprivate.compiler

import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.MppCheckerKind
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirCallableDeclarationChecker
import org.jetbrains.kotlin.fir.analysis.checkers.expression.ExpressionCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.expression.FirCallChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.declarations.FirCallableDeclaration
import org.jetbrains.kotlin.fir.declarations.getAnnotationByClassId
import org.jetbrains.kotlin.fir.declarations.toAnnotationClassIdSafe
import org.jetbrains.kotlin.fir.expressions.FirCall
import org.jetbrains.kotlin.fir.expressions.FirResolvable
import org.jetbrains.kotlin.fir.references.toResolvedCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirCallableSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.types.toRegularClassSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

class PackagePrivateChecker(session: FirSession) : FirAdditionalCheckersExtension(session) {
    override val expressionCheckers: ExpressionCheckers = object : ExpressionCheckers() {
        override val callCheckers: Set<FirCallChecker> = setOf(PackagePrivateCallChecker)
    }
    
    override val declarationCheckers: DeclarationCheckers = object : DeclarationCheckers() {
        override val callableDeclarationCheckers: Set<FirCallableDeclarationChecker> = 
            setOf(RedundantPackagePrivateChecker)
    }
}

private val PACKAGE_PRIVATE_CLASS_ID = ClassId(
    FqName("com.acme.packageprivate"),
    org.jetbrains.kotlin.name.Name.identifier("PackagePrivate")
)

private object PackagePrivateCallChecker : FirCallChecker(MppCheckerKind.Common) {
    override fun check(expression: FirCall, context: CheckerContext, reporter: DiagnosticReporter) {
        val containingFile = context.containingFile ?: return
        val callerPackage = containingFile.packageDirective.packageFqName

        val resolvable = expression as? FirResolvable ?: return
        val symbol = resolvable.calleeReference.toResolvedCallableSymbol() ?: return

        // Check if the called symbol itself is package-private
        checkSymbol(symbol, callerPackage, expression, context, reporter)

        // For constructors, also check if the containing class is package-private
        if (symbol is FirConstructorSymbol) {
            val classSymbol = symbol.resolvedReturnType.toRegularClassSymbol(context.session)
            classSymbol?.let { cls ->
                val annotation = cls.annotations.getAnnotationByClassId(PACKAGE_PRIVATE_CLASS_ID, context.session)
                if (annotation != null) {
                    val scopeOverride = extractScopeFromAnnotation(annotation)
                    val targetPackage = if (scopeOverride.isNotEmpty()) {
                        FqName(scopeOverride)
                    } else {
                        cls.classId.packageFqName
                    }
                    if (callerPackage != targetPackage) {
                        reporter.reportOn(
                            expression.source,
                            PackagePrivateErrors.PACKAGE_PRIVATE_ACCESS,
                            cls.classId.asSingleFqName().asString(),
                            targetPackage.asString(),
                            context
                        )
                    }
                }
            }
        }
    }

    private fun checkSymbol(
        symbol: FirCallableSymbol<*>,
        callerPackage: FqName,
        expression: FirCall,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        val annotation = symbol.annotations.getAnnotationByClassId(PACKAGE_PRIVATE_CLASS_ID, context.session)
            ?: return

        val scopeOverride = extractScopeFromAnnotation(annotation)
        val targetPackage = if (scopeOverride.isNotEmpty()) {
            FqName(scopeOverride)
        } else {
            symbol.callableId.packageName
        }

        if (callerPackage != targetPackage) {
            reporter.reportOn(
                expression.source,
                PackagePrivateErrors.PACKAGE_PRIVATE_ACCESS,
                symbol.callableId.asSingleFqName().asString(),
                targetPackage.asString(),
                context
            )
        }
    }

    private fun extractScopeFromAnnotation(annotation: org.jetbrains.kotlin.fir.expressions.FirAnnotation): String {
        val argument = annotation.argumentMapping.mapping.entries.firstOrNull {
            it.key.asString() == "scope"
        }?.value
        return (argument as? org.jetbrains.kotlin.fir.expressions.FirLiteralExpression)?.value as? String ?: ""
    }
}

private object RedundantPackagePrivateChecker : FirCallableDeclarationChecker(MppCheckerKind.Common) {
    override fun check(
        declaration: FirCallableDeclaration,
        context: CheckerContext,
        reporter: DiagnosticReporter
    ) {
        // Check if this declaration has @PackagePrivate
        val hasPackagePrivate = declaration.annotations.any { 
            it.toAnnotationClassIdSafe(context.session) == PACKAGE_PRIVATE_CLASS_ID 
        }
        if (!hasPackagePrivate) return
        
        // Check if containing class also has @PackagePrivate
        val containingClass = declaration.dispatchReceiverType?.toRegularClassSymbol(context.session) ?: return
        val classHasPackagePrivate = containingClass.annotations.any {
            it.toAnnotationClassIdSafe(context.session) == PACKAGE_PRIVATE_CLASS_ID
        }
        
        if (classHasPackagePrivate) {
            reporter.reportOn(
                declaration.source,
                PackagePrivateErrors.REDUNDANT_PACKAGE_PRIVATE,
                declaration.symbol.callableId.callableName.asString(),
                context
            )
        }
    }
}
