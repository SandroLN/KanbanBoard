package com.github.sandroln.kanbanboard.profile.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface ProfileCommunication : Communication.Mutable<ProfileUiState> {
    class Base : Communication.Single<ProfileUiState>(), ProfileCommunication
}