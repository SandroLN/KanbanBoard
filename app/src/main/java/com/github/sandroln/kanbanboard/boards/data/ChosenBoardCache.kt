package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo
import com.github.sandroln.kanbanboard.core.ObjectStorage

interface ChosenBoardCache {
    interface Save : com.github.sandroln.kanbanboard.core.Save<BoardInfo>
    interface Read : com.github.sandroln.kanbanboard.core.Read<BoardInfo>
    interface Mutable : Save, Read

    class Base(
        private val objectStorage: ObjectStorage.Mutable,
        private val key: String = "ChosenBoardCache"
    ) : Mutable {

        override fun save(data: BoardInfo) = objectStorage.save(key, data)
        override fun read(): BoardInfo = objectStorage.read(key, BoardInfo("", "", true))
    }
}