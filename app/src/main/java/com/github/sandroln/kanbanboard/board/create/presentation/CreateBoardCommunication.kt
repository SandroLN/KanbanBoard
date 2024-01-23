package com.github.sandroln.kanbanboard.board.create.presentation

import com.github.sandroln.core.Communication

interface CreateBoardCommunication : Communication.Mutable<CreateBoardUiState> {
    class Base : Communication.Single<CreateBoardUiState>(), CreateBoardCommunication
}