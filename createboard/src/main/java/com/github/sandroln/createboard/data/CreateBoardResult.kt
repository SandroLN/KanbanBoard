package com.github.sandroln.createboard.data

import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.createboard.presentation.CreateBoardCommunication
import com.github.sandroln.createboard.presentation.CreateBoardUiState

internal interface CreateBoardResult {

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