package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.core.StringStorage

interface EditTicketIdCache {

    interface Save : com.github.sandroln.kanbanboard.core.Save<String>
    interface Read : com.github.sandroln.kanbanboard.core.Read<String>
    interface Mutable : Save, Read

    class Base(
        private val stringStorage: StringStorage.Mutable,
        private val key: String = "EditTicketIdCache"
    ) : Mutable {

        override fun save(data: String) = stringStorage.save(key, data)

        override fun read() = stringStorage.read(key, "")
    }
}