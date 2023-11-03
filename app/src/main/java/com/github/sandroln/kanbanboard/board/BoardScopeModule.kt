package com.github.sandroln.kanbanboard.board

import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.ContainerBoardAllData

interface BoardScopeModule {

    fun provideContainer(): ContainerBoardAllData

    fun provideMembers(): BoardMembersCommunication.Mutable

    class Base : BoardScopeModule {

        private val boardMembersCommunication = BoardMembersCommunication.Base()
        private val containerBoardAllData =
            ContainerBoardAllData.Base(boardMembersCommunication)

        override fun provideContainer() = containerBoardAllData

        override fun provideMembers() = boardMembersCommunication
    }
}

interface ProvideBoardScopeModule {
    fun boardScopeModule(): BoardScopeModule
}

interface ClearBoardScopeModule {
    fun clearBoardScopeModule()
}