package com.github.sandroln.chosenboard

interface BoardScreenNavigation {

    fun navigateToBoard()

    class Base(
        private val clearBoardScopeModule: ClearBoardScopeModule,
        private val navigation: BoardScreenNavigation
    ) : BoardScreenNavigation {

        override fun navigateToBoard() {
            clearBoardScopeModule.clearBoardScopeModule()
            navigation.navigateToBoard()
        }
    }
}