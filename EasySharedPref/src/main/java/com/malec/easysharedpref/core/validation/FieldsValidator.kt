package com.malec.easysharedpref.core.validation

import com.google.devtools.ksp.getDeclaredProperties
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.malec.easysharedpref.core.UnsupportedTypeException

class FieldsValidator(private val supportedTypes: Set<String>) {
    @Throws(UnsupportedTypeException::class)
    fun validate(declaration: KSClassDeclaration) {
        for (field in declaration.getDeclaredProperties()) {
            if (!isValid(field.type))
                throw UnsupportedTypeException(field.type)
        }
    }

    private fun isValid(type: KSTypeReference): Boolean {
        val name = type.toString()
        return name in supportedTypes
    }
}