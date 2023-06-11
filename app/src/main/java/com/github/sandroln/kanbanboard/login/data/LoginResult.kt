package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.login.presentation.LoginCommunication
import com.github.sandroln.kanbanboard.login.presentation.LoginUiState
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.profile.presentation.ProfileScreen

interface LoginResult {

    fun map(communication: LoginCommunication, navigation: NavigationCommunication.Update)

    object Success : LoginResult {
        override fun map(
            communication: LoginCommunication,
            navigation: NavigationCommunication.Update
        ) = navigation.map(ProfileScreen)

    }

    data class Failed(private val message: String) : LoginResult {
        override fun map(
            communication: LoginCommunication,
            navigation: NavigationCommunication.Update
        ) = communication.map(LoginUiState.Error(message))
    }
}