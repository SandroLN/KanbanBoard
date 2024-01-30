package com.github.sandroln.board.presentation

import com.github.sandroln.core.Communication

internal interface BoardCommunication : Communication.Mutable<BoardUiState> {
    class Base : Communication.Single<BoardUiState>(), BoardCommunication
}