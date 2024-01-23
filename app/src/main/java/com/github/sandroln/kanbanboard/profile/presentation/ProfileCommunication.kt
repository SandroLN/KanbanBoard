package com.github.sandroln.kanbanboard.profile.presentation

interface ProfileCommunication : com.github.sandroln.core.Communication.Mutable<ProfileUiState> {
    class Base : com.github.sandroln.core.Communication.Single<ProfileUiState>(), ProfileCommunication
}