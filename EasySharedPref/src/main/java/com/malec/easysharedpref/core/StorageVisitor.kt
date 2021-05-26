package com.malec.easysharedpref.core

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid

class StorageVisitor : KSVisitorVoid() {
    private var storage: KSClassDeclaration? = null

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        storage = classDeclaration
    }

    fun getStorage() = storage
}