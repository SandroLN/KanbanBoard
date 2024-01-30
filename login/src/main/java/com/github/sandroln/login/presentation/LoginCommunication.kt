package com.github.sandroln.login.presentation

import com.github.sandroln.core.Communication

internal interface LoginCommunication : Communication.Mutable<LoginUiState> {
    class Base : Communication.Single<LoginUiState>(), LoginCommunication
}