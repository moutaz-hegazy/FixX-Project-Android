package com.example.fixx.POJOs

import java.io.Serializable

data class Extension (
    var extId : String? = null, var images : MutableList<StringPair>? = null,
    val description : String? = null, var price : Int? = null): Serializable