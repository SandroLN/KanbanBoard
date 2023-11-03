package com.github.sandroln.kanbanboard.board.create.data

import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardCommunication
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardUiState
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreenNavigation

interface CreateBoardResult {

    fun map(boardScreenNavigation: BoardScreenNavigation, communication: CreateBoardCommunication)

    object Success : CreateBoardResult {
        override fun map(
            boardScreenNavigation: BoardScreenNavigation,
            communication: CreateBoardCommunication
        ) = boardScreenNavigation.navigateToBoard()
    }

    data class Failed(private val errorMessage: String) : CreateBoardResult {
        override fun map(
            boardScreenNavigation: BoardScreenNavigation,
            communication: CreateBoardCommunication
        ) = communication.map(CreateBoardUiState.Error(errorMessage))
    }
}