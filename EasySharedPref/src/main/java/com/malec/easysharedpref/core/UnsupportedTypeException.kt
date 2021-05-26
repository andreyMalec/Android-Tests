package com.malec.easysharedpref.core

import com.google.devtools.ksp.symbol.KSTypeReference

class UnsupportedTypeException(type: KSTypeReference) :
    RuntimeException("SharedPreferences does not support $type")