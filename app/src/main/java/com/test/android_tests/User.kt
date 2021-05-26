package com.test.android_tests

import com.malec.easysharedpref.PrefEntity

@PrefEntity
data class User(
    val name: String,
    val age: Int
)