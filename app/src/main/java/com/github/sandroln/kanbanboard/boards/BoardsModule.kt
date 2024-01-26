package com.github.sandroln.kanbanboard.boards

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreenNavigation
import com.github.sandroln.kanbanboard.boards.data.BoardsCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.BoardsRepository
import com.github.sandroln.kanbanboard.boards.data.MyBoardsNamesCache
import com.github.sandroln.kanbanboard.boards.presentation.BoardsCommunication
import com.github.sandroln.kanbanboard.boards.presentation.BoardsViewModel
import com.github.sandroln.kanbanboard.core.CoreImpl

class BoardsModule(private val core: CoreImpl) : Module<BoardsViewModel> {

    override fun viewModel() = BoardsViewModel(
        core.provideMyUser(),
        BoardScreenNavigation.Base(core, core.navigation()),
        core.navigation(),
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