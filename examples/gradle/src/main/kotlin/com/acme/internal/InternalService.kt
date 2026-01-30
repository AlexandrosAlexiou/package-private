package com.acme.internal

/**
 * This service uses all internal declarations within the same package.
 * The analyzer will suggest adding @PackagePrivate to declarations
 * that are only used within this package.
 */
class InternalService {
    private val helper = InternalHelper()
    
    fun doWork(): String {
        val result = helper.compute()
        val util = utilityFunction()
        return "Result: $result, Util: $util"
    }
    
    // Uses the enum, sealed class and typealias
    fun getStatus(): InternalStatus = InternalStatus.ACTIVE
    
    fun process(): InternalResult = InternalResult.Success(42)
    
    fun withCallback(cb: InternalCallback) = cb(42)
    
    // Uses property and extensions
    fun usePropertyAndExtensions(): String {
        val config = internalConfig
        val formatted = "test".toInternalFormat()
        val len = "test".internalLength
        return "$config - $formatted - $len"
    }
    
    // Uses nested class
    fun useNested(): Int = Container.Nested().compute()
    
    // Uses inheritance
    fun useInheritance(): Int {
        val impl: InternalContract = InternalImpl()
        return impl.execute().length
    }
    
    // Uses generics
    fun useGenerics(): List<InternalEntity> {
        val repo = InternalRepository()
        repo.add(InternalEntity(1, "test"))
        return repo.getAll()
    }
}
