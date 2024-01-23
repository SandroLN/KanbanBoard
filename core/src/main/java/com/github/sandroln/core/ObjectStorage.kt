package com.github.sandroln.core

import com.google.gson.Gson

interface ObjectStorage {

    interface Save {
        fun save(key: String, obj: Any)
    }

    interface Read {
        fun <T : Any> read(key: String, default: T): T
    }

    interface Mutable : Save, Read

    class Base(
        private val storage: StringStorage.Mutable,
        private val gson: Gson
    ) : Mutable {

        override fun save(key: String, obj: Any) {
            val value = gson.toJson(obj)
            storage.save(key, value)
        }

        override fun <T : Any> read(key: String, default: T): T {
            val string = storage.read(key, gson.toJson(default))
            return gson.fromJson(string, default::class.java)
        }
    }
}