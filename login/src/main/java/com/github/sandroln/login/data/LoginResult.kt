package com.github.sandroln.login.data

import com.github.sandroln.login.presentation.LoginCommunication
import com.github.sandroln.login.presentation.LoginUiState

internal interface LoginResult {

    fun map(communication: LoginCommunication, navigation: NavigateFromLogin)

    object Success : LoginResult {

        override fun map(
            communication: LoginCommunication,
            navigation: NavigateFromLogin
        ) = navigation.navigateFromLogin()
    }

    data class Failed(private val message: String) : LoginResult {

        override fun map(
            communication: LoginCommunication,
            navigation: NavigateFromLogin
        ) = communication.map(LoginUiState.Error(message))
    }
}