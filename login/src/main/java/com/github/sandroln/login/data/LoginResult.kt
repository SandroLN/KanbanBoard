package com.github.sandroln.login.data

import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.login.presentation.LoginCommunication
import com.github.sandroln.login.presentation.LoginUiState

internal interface LoginResult {

    fun map(communication: LoginCommunication, navigation: NavigateToBoards)

    object Success : LoginResult {

        override fun map(
            communication: LoginCommunication,
            navigation: NavigateToBoards
        ) = navigation.navigateToBoards()
    }

    data class Failed(private val message: String) : LoginResult {

        override fun map(
            communication: LoginCommunication,
            navigation: NavigateToBoards
        ) = communication.map(LoginUiState.Error(message))
    }
}