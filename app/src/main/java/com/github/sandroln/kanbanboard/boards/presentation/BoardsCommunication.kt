package com.github.sandroln.kanbanboard.boards.presentation

interface BoardsCommunication : com.github.sandroln.core.Communication.Mutable<BoardsUiState> {
    class Base : com.github.sandroln.core.Communication.Single<BoardsUiState>(), BoardsCommunication
}