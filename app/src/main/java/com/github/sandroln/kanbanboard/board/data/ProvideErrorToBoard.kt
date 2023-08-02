package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.BoardCommunication
import com.github.sandroln.kanbanboard.board.presentation.BoardUiState
import com.github.sandroln.kanbanboard.core.ProvideError

class ProvideErrorToBoard(
    private val boardCommunication: BoardCommunication,
) : ProvideError {

    override fun error(message: String) = boardCommunication.map(BoardUiState.Error(message))
}