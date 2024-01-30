package com.github.sandroln.kanbanboard.core

import com.github.sandroln.board.BoardNavigationList
import com.github.sandroln.board.presentation.BoardScreen
import com.github.sandroln.boards.main.BoardsNavigationList
import com.github.sandroln.boards.main.presentation.BoardsScreen
import com.github.sandroln.boardssettings.presentation.BoardSettingsScreen
import com.github.sandroln.cloudservice.NavigateToLoginScreen
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.createboard.CreateBoardNavigationList
import com.github.sandroln.createboard.presentation.CreateBoardScreen
import com.github.sandroln.createticket.presentation.CreateTicketScreen
import com.github.sandroln.editticket.presentation.EditTicketScreen
import com.github.sandroln.login.presentation.LoginScreen
import com.github.sandroln.profile.presentation.ProfileScreen

interface FeaturesNavigation :
    NavigateToLoginScreen,
    BoardsNavigationList,
    CreateBoardNavigationList,
    BoardNavigationList {

    class Base(
        private val navigation: NavigationCommunication.Update
    ) : FeaturesNavigation {

        override fun navigateToBoards() = navigation.map(BoardsScreen)
        override fun navigateToCreateTicket() = navigation.map(CreateTicketScreen)
        override fun navigateToEditTicket() = navigation.map(EditTicketScreen)
        override fun navigateToBoardSettings() = navigation.map(BoardSettingsScreen)
        override fun navigateToLoginScreen() = navigation.map(LoginScreen)
        override fun navigateToBoard() = navigation.map(BoardScreen)
        override fun navigateFromBoards() = navigation.map(ProfileScreen)
        override fun navigateToCreateBoard() = navigation.map(CreateBoardScreen)
    }
}