package com.github.sandroln.profile.presentation

import com.github.sandroln.core.Communication

internal interface ProfileCommunication : Communication.Mutable<ProfileUiState> {
    class Base : Communication.Single<ProfileUiState>(),
        ProfileCommunication
}