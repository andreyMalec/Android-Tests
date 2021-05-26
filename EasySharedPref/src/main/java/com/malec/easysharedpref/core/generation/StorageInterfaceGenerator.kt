package com.malec.easysharedpref.core.generation

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration

class StorageInterfaceGenerator(
    override val generator: CodeGenerator,
    override val declaration: KSClassDeclaration
) : Generator(generator, declaration) {

    override val packageName = declaration.containingFile?.packageName?.asString() ?: ""
    override val className = "${declaration.simpleName.asString()}Storage"

    init {
        createFile {
            wrappedBlock("interface $className") {
                for (field in declaration.getDeclaredProperties()) {
                    val type = field.type.toString().takeIf { it != "Integer" } ?: "Int"
                    line("var ${field.simpleName.asString()}: $type")
                }
                line("fun clear()")
            }
        }
    }
}