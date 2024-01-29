package com.github.sandroln.openedboard

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