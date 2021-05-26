package com.malec.easysharedpref.core.generation

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration

class StorageImplGenerator(
    override val generator: CodeGenerator,
    override val declaration: KSClassDeclaration
) : Generator(generator, declaration) {

    override val packageName = declaration.containingFile?.packageName?.asString() ?: ""
    private val interfaceName = "${declaration.simpleName.asString()}Storage"
    override val className = "${interfaceName}Impl"

    init {
        createFile {
            import("android.content.Context", "com.malec.easysharedprefs.*")
            wrappedBlock("class $className(context: Context): $interfaceName") {
                line("private val storage = context.getSharedPreferences(\"$interfaceName\", Context.MODE_PRIVATE)")
                for (field in declaration.getDeclaredProperties()) {
                    val name = field.simpleName.asString()
                    val type = field.type.toString().takeIf { it != "Integer" } ?: "Int"
                    accessor(
                        "override var $name: $type",
                        "get() = storage[\"$name\"]",
                        "set(value) { storage[\"$name\"] = value }"
                    )
                }
                wrappedBlock("override fun clear()") {
                    line("storage.clear()")
                }
            }
        }
    }
}