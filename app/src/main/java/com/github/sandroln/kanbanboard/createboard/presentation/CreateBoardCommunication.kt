package com.github.sandroln.kanbanboard.createboard.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface CreateBoardCommunication : Communication.Mutable<CreateBoardUiState> {
    class Base : Communication.Single<CreateBoardUiState>(), CreateBoardCommunication
}