package com.github.sandroln.boards.main.data

import com.github.sandroln.boards.main.presentation.BoardUi

internal interface Board {

    fun toUi(): BoardUi

    object MyOwnBoardsTitle : Board {
        override fun toUi() = BoardUi.MyBoardTitle
    }

    data class MyBoard(private val key: String, private val name: String) : Board {
        override fun toUi() = BoardUi.MyBoard(key, name)
    }

    object NoBoardsOfMyOwnHint : Board {
        override fun toUi() = BoardUi.NoBoardsOfMyOwnHint
    }

    object OtherBoardsTitle : Board {
        override fun toUi() = BoardUi.OtherBoardsTitle
    }

    data class OtherBoard(
        private val key: String,
        private val name: String,
        private val owner: String
    ) : Board {
        override fun toUi() = BoardUi.OtherBoard(key, name, owner)
    }

    object HowToBeAddedToBoardHint : Board {
        override fun toUi() = BoardUi.HowToBeAddedToBoardHint
    }

    data class Error(private val message: String) : Board {
        override fun toUi() = BoardUi.Error(message)
    }
}