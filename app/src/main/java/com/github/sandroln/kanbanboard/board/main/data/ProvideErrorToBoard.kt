package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.board.main.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.BoardUiState

class ProvideErrorToBoard(
    private val boardCommunication: BoardCommunication,
) : com.github.sandroln.core.ProvideError {

    override fun error(message: String) = boardCommunication.map(BoardUiState.Error(message))
}