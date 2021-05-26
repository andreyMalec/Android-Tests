package com.malec.easysharedpref.core.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.symbol.KSClassDeclaration

class SharedPrefsExtGenerator(
    override val generator: CodeGenerator,
    override val declaration: KSClassDeclaration
) : Generator(generator, declaration) {

    override val packageName = "com.malec.easysharedprefs"
    override val className = "SharedPrefsExt"

    init {
        createFile {
            import("android.content.SharedPreferences")
            wrappedBlock("inline operator fun <reified T : Any> SharedPreferences.get(key: String): T") {
                wrappedBlock("return when (T::class)") {
                    line("Int::class -> getInt(key, 0) as T")
                    line("String::class -> getString(key, \"\") as T")
                    line("Set::class -> getStringSet(key, setOf()) as T")
                    line("Long::class -> getLong(key, 0) as T")
                    line("Float::class -> getFloat(key, 0f) as T")
                    line("Boolean::class -> getBoolean(key, false) as T")
                    line("else -> throw UnsupportedOperationException(\"Not yet implemented\")")
                }
            }
            line("")
            wrappedBlock("operator fun SharedPreferences.set(key: String, value: Any)") {
                wrappedBlock("when (value)") {
                    line("is Int -> edit { it.putInt(key, value) }")
                    line("is String -> edit { it.putString(key, value) }")
                    line("is Set<*> -> edit { it.putStringSet(key, value.map { it.toString() }.toSet()) }")
                    line("is Long -> edit { it.putLong(key, value) }")
                    line("is Float -> edit { it.putFloat(key, value) }")
                    line("is Boolean -> edit { it.putBoolean(key, value) }")
                    line("else -> throw UnsupportedOperationException(\"Not yet implemented\")")
                }
            }
            line("")
            wrappedBlock("private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit)") {
                line("val editor = this.edit()")
                line("operation(editor)")
                line("editor.apply()")
            }
            line("")
            wrappedBlock("fun SharedPreferences.clear()") {
                line("edit { it.clear() }")
            }
        }
    }
}