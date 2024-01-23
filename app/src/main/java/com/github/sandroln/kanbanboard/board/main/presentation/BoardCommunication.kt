package com.github.sandroln.kanbanboard.board.main.presentation

import com.github.sandroln.core.Communication

interface BoardCommunication : Communication.Mutable<BoardUiState> {
    class Base : Communication.Single<BoardUiState>(), BoardCommunication
}