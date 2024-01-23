package com.github.sandroln.kanbanboard.board.main.data

interface EditTicketIdCache {

    interface Save : com.github.sandroln.core.Save<String>
    interface Read : com.github.sandroln.core.Read<String>
    interface Mutable : Save, Read

    class Base(
        private val stringStorage: com.github.sandroln.core.StringStorage.Mutable,
        private val key: String = "EditTicketIdCache"
    ) : Mutable {

        override fun save(data: String) = stringStorage.save(key, data)

        override fun read() = stringStorage.read(key, "")
    }
}