package com.github.sandroln.board.data

import com.github.sandroln.board.presentation.BoardCommunication
import com.github.sandroln.board.presentation.BoardUiState
import com.github.sandroln.core.ProvideError

internal class ProvideErrorToBoard(
    private val boardCommunication: BoardCommunication,
) : ProvideError {

    override fun error(message: String) = boardCommunication.map(BoardUiState.Error(message))
}