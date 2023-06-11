package com.github.sandroln.kanbanboard.boards

import com.github.sandroln.kanbanboard.boards.data.BoardsCloudDataSource
import com.github.sandroln.kanbanboard.boards.data.BoardsRepository
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.boards.data.MyBoardsNamesCache
import com.github.sandroln.kanbanboard.boards.presentation.BoardsCommunication
import com.github.sandroln.kanbanboard.boards.presentation.BoardsViewModel
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module


class BoardsModule(private val core: Core) : Module<BoardsViewModel> {

    override fun viewModel() = BoardsViewModel(
        core.navigation(),
        core.provideDispatchersList(),
        BoardsRepository.Base(
            ChosenBoardCache.Base(core.storage()),
            BoardsCloudDataSource.Base(
                MyBoardsNamesCache.Base(core.storage()),
                core
            )
        ),
        BoardsCommunication.Base()
    )
}