package com.github.sandroln.chosenboard

import com.github.sandroln.core.ObjectStorage


interface ChosenBoardCache {
    interface Save : com.github.sandroln.core.Save<BoardCache>
    interface Read : com.github.sandroln.core.Read<BoardCache>
    interface Mutable : Save, Read

    class Base(
        private val objectStorage: ObjectStorage.Mutable,
        private val key: String = "ChosenBoardCache"
    ) : Mutable {

        override fun save(data: BoardCache) = objectStorage.save(key, data)
        override fun read(): BoardCache = objectStorage.read(key, BoardCache("", "", true))
    }
}