package com.github.sandroln.boards.main.presentation

internal interface BoardsCommunication : com.github.sandroln.core.Communication.Mutable<BoardsUiState> {
    class Base : com.github.sandroln.core.Communication.Single<BoardsUiState>(), BoardsCommunication
}