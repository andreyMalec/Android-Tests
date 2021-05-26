package com.malec.easysharedpref.core.generation

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSDeclaration
import java.io.OutputStream

abstract class Generator(protected open val generator: CodeGenerator, protected open val declaration: KSDeclaration) {
    protected abstract val packageName: String
    protected abstract val className: String

    protected var indent = 0

    protected inline fun createFile(block: OutputStream.() -> Unit) {
        val file = generator.createNewFile(Dependencies(true, declaration.containingFile!!), packageName, className)

        with(file) {
            append("package $packageName\n\n")
            block(file)

            close()
        }
    }

    protected inline fun OutputStream.wrappedBlock(title: String, block: OutputStream.() -> Unit) {
        append("$title {\n")
        indent++
        block(this)
        indent--
        append("}\n")
    }

    protected fun OutputStream.line(str: String) {
        val newLine = if (indent == 1) "\n" else ""
        write("${tab.repeat(indent)}${str}\n$newLine".toByteArray())
    }

    protected fun OutputStream.accessor(title: String, vararg accessors: String) {
        append("$title\n")
        indent++
        accessors.forEach { accessor ->
            append("$accessor\n")
        }
        indent--
        append("\n")
    }

    protected fun OutputStream.import(vararg imports: String) {
        imports.forEach { import ->
            write("import ${import}\n".toByteArray())
        }
        append("\n")
    }

    protected fun OutputStream.append(str: String) {
        write("${tab.repeat(indent)}${str}".toByteArray())
    }

    companion object {
        private const val tab = "   "
    }
}