package com.github.sandroln.kanbanboard.boards.data


interface MyBoardsNamesCache {
    interface Save : com.github.sandroln.core.Save<List<String>>
    interface Read : com.github.sandroln.core.Read<List<String>>
    interface Mutable : Save, Read

    class Base(
        private val objectStorage: com.github.sandroln.core.ObjectStorage.Mutable,
        private val key: String = "MyBoardsNamesCache"
    ) : Mutable {

        override fun save(data: List<String>) = objectStorage.save(key, Wrapper(data))

        override fun read() = objectStorage.read(key, Wrapper(listOf())).list
    }

    private data class Wrapper(val list: List<String>)
}