package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.core.ObjectStorage


interface MyBoardsNamesCache {
    interface Save : com.github.sandroln.kanbanboard.core.Save<List<String>>
    interface Read : com.github.sandroln.kanbanboard.core.Read<List<String>>
    interface Mutable : Save, Read

    class Base(
        private val objectStorage: ObjectStorage.Mutable,
        private val key: String = "MyBoardsNamesCache"
    ) : Mutable {

        override fun save(data: List<String>) = objectStorage.save(key, Wrapper(data))

        override fun read() = objectStorage.read(key, Wrapper(listOf())).list
    }

    private data class Wrapper(val list: List<String>)
}