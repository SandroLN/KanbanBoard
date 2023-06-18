package com.github.sandroln.kanbanboard.board.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface BoardCommunication : Communication.Mutable<BoardUiState> {
    class Base : Communication.Abstract<BoardUiState>(), BoardCommunication
}