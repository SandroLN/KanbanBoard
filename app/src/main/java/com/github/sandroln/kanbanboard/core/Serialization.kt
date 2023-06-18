package com.github.sandroln.kanbanboard.core

import com.google.gson.Gson

interface Serialization {

    interface From {
        fun toString(obj: Any): String
    }

    interface To {
        fun <T : Any> fromString(source: String, clasz: Class<T>): T
    }

    interface Mutable : To, From

    class Base(private val gson: Gson) : Mutable {

        override fun toString(obj: Any): String = gson.toJson(obj)

        override fun <T : Any> fromString(source: String, clasz: Class<T>): T =
            gson.fromJson(source, clasz)
    }
}