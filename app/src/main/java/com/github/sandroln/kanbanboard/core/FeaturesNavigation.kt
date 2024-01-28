package com.github.sandroln.kanbanboard.core

import com.github.sandroln.boards.main.BoardsNavigationList
import com.github.sandroln.boards.main.presentation.BoardsScreen
import com.github.sandroln.cloudservice.NavigateToLoginScreen
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.createboard.CreateBoardNavigationList
import com.github.sandroln.createboard.presentation.CreateBoardScreen
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreen
import com.github.sandroln.login.presentation.LoginScreen
import com.github.sandroln.profile.presentation.ProfileScreen

interface FeaturesNavigation :
    NavigateToLoginScreen,
    BoardsNavigationList,
    CreateBoardNavigationList {

    class Base(
        private val navigation: NavigationCommunication.Update
    ) : FeaturesNavigation {

        override fun navigateToBoards() = navigation.map(BoardsScreen)
        override fun navigateToLoginScreen() = navigation.map(LoginScreen)
        override fun navigateToBoard() = navigation.map(BoardScreen)
        override fun navigateFromBoards() = navigation.map(ProfileScreen)
        override fun navigateToCreateBoard() = navigation.map(CreateBoardScreen)
    }
}