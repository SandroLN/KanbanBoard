package com.github.sandroln.boards.main

import com.github.sandroln.boards.main.data.BoardsCloudDataSource
import com.github.sandroln.boards.main.data.BoardsRepository
import com.github.sandroln.boards.main.presentation.BoardsCommunication
import com.github.sandroln.boards.main.presentation.BoardsNavigation
import com.github.sandroln.boards.main.presentation.BoardsViewModel
import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.chosenboard.ClearBoardScopeModule
import com.github.sandroln.chosenboard.MyBoardsNamesCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module

internal class BoardsModule(
    private val navigation: BoardsNavigationList,
    private val core: Core,
    private val clearBoardScopeModule: ClearBoardScopeModule
) : Module<BoardsViewModel> {

    override fun viewModel() = BoardsViewModel(
        navigation,
        core.provideMyUser(),
        BoardScreenNavigation.Base(clearBoardScopeModule, navigation),
        core.provideDispatchersList(),
        BoardsRepository.Base(
            ChosenBoardCache.Base(core.storage()),
            BoardsCloudDataSource.Base(
                core.provideMyUser(),
                MyBoardsNamesCache.Base(core.storage()),
                core.service()
            )
        ),
        BoardsCommunication.Base()
    )
}

interface BoardsNavigationList : BoardScreenNavigation, BoardsNavigation