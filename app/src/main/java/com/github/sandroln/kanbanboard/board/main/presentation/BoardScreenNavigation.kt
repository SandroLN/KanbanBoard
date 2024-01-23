package com.github.sandroln.kanbanboard.board.main.presentation

import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.kanbanboard.board.ClearBoardScopeModule

interface BoardScreenNavigation {

    fun navigateToBoard()

    class Base(
        private val clearBoardScopeModule: ClearBoardScopeModule,
        private val navigation: NavigationCommunication.Update
    ) : BoardScreenNavigation {

        override fun navigateToBoard() {
            clearBoardScopeModule.clearBoardScopeModule()
            navigation.map(BoardScreen)
        }
    }
}