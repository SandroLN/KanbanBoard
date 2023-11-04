package com.github.sandroln.kanbanboard.boards.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface BoardsCommunication : Communication.Mutable<BoardsUiState> {
    class Base : Communication.Single<BoardsUiState>(), BoardsCommunication
}