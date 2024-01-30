package com.github.sandroln.createboard.presentation

import com.github.sandroln.core.Communication

internal interface CreateBoardCommunication : Communication.Mutable<CreateBoardUiState> {
    class Base : Communication.Single<CreateBoardUiState>(), CreateBoardCommunication
}