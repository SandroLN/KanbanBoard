package com.github.sandroln.core

import android.content.SharedPreferences

interface Storage : SimpleStorage, ObjectStorage.Mutable {

    class Base(
        private val simpleStorage: SimpleStorage,
        private val objectStorage: ObjectStorage.Mutable
    ) : Storage {

        override fun save(key: String, value: String) = simpleStorage.save(key, value)

        override fun save(key: String, value: Boolean) = simpleStorage.save(key, value)

        override fun save(key: String, obj: Any) = objectStorage.save(key, obj)

        override fun read(key: String, default: String) = simpleStorage.read(key, default)

        override fun read(key: String, default: Boolean) = simpleStorage.read(key, default)

        override fun <T : Any> read(key: String, default: T): T = objectStorage.read(key, default)
    }
}

interface SimpleStorage : StringStorage.Mutable, BooleanStorage.Mutable {

    class Base(private val sharedPreferences: SharedPreferences) : SimpleStorage {

        override fun save(key: String, value: Boolean) =
            sharedPreferences.edit().putBoolean(key, value).apply()

        override fun save(key: String, value: String) =
            sharedPreferences.edit().putString(key, value).apply()

        override fun read(key: String, default: Boolean) =
            sharedPreferences.getBoolean(key, default)

        override fun read(key: String, default: String): String =
            sharedPreferences.getString(key, default) ?: default
    }
}

interface StringStorage {
    interface Save {
        fun save(key: String, value: String)
    }

    interface Read {
        fun read(key: String, default: String): String
    }

    interface Mutable : Save, Read
}

interface BooleanStorage {
    interface Save {
        fun save(key: String, value: Boolean)
    }

    interface Read {
        fun read(key: String, default: Boolean): Boolean
    }

    interface Mutable : Save, Read
}