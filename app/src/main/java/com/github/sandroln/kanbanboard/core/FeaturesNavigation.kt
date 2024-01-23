package com.github.sandroln.kanbanboard.core

import com.github.sandroln.cloudservice.NavigateToLoginScreen
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.kanbanboard.boards.presentation.BoardsScreen
import com.github.sandroln.login.data.NavigateFromLogin
import com.github.sandroln.login.presentation.LoginScreen

interface FeaturesNavigation : NavigateFromLogin, NavigateToLoginScreen {

    class Base(
        private val navigation: NavigationCommunication.Update
    ) : FeaturesNavigation {

        override fun navigateFromLogin() = navigation.map(BoardsScreen)
        override fun navigateToLoginScreen() = navigation.map(LoginScreen)
    }
}