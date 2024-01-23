package com.github.sandroln.kanbanboard.boards.presentation

import com.github.sandroln.kanbanboard.boards.data.Board

interface BoardsUiState {

    fun show(mapper: com.github.sandroln.core.Mapper.Unit<List<BoardUi>>)

    abstract class Abstract(private val list: List<BoardUi>) : BoardsUiState {
        override fun show(mapper: com.github.sandroln.core.Mapper.Unit<List<BoardUi>>) = mapper.map(list)
    }

    data class Base(private val list: List<Board>) : Abstract(list.map { it.toUi() })

    abstract class Single(boardUi: BoardUi) : Abstract(listOf(boardUi))

    object Progress : Single(BoardUi.Progress)
    class Error(message: String) : Single(BoardUi.Error(message))
}