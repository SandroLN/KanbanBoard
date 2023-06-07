package com.github.sandroln.kanbanboard.login.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface LoginCommunication : Communication.Mutable<LoginUiState> {
    class Base : Communication.Abstract<LoginUiState>(), LoginCommunication
}