package com.malec.easysharedpref

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.malec.easysharedpref.core.StorageVisitor
import com.malec.easysharedpref.core.UnsupportedTypeException
import com.malec.easysharedpref.core.generation.SharedPrefsExtGenerator
import com.malec.easysharedpref.core.generation.StorageImplGenerator
import com.malec.easysharedpref.core.generation.StorageInterfaceGenerator
import com.malec.easysharedpref.core.validation.FieldsValidator

class PrefProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    private var first = true

    private val validator =
        FieldsValidator(setOf("Int", "Integer", "String", "Set<String>", "Long", "Float", "Boolean"))

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation("com.malec.easysharedpref.PrefEntity")
        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                val visitor = StorageVisitor()
                it.accept(visitor, Unit)
                visitor.getStorage()?.let { storage ->
                    if (first) {
                        first = false
                        SharedPrefsExtGenerator(codeGenerator, storage)
                    }
                    try {
                        validator.validate(storage)
                    } catch (e: UnsupportedTypeException) {
                        logger.error(e.localizedMessage)
                    }
                    StorageInterfaceGenerator(codeGenerator, storage)
                    StorageImplGenerator(codeGenerator, storage)
                }
            }
        return ret
    }
}