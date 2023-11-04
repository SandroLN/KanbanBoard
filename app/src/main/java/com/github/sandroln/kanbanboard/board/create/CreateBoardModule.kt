package com.github.sandroln.kanbanboard.board.create

import com.github.sandroln.kanbanboard.board.create.data.CreateBoardCloudDataSource
import com.github.sandroln.kanbanboard.board.create.data.CreateBoardRepository
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardCommunication
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardViewModel
import com.github.sandroln.kanbanboard.board.main.presentation.BoardScreenNavigation
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.boards.data.MyBoardsNamesCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class CreateBoardModule(private val core: Core) : Module<CreateBoardViewModel> {

    override fun viewModel() = CreateBoardViewModel(
        BoardScreenNavigation.Base(core, core.navigation()),
        core.manageResource(),
        CreateBoardRepository.Base(
            CreateBoardCloudDataSource.Base(core.provideMyUser(), core),
            ChosenBoardCache.Base(core.storage()),
            MyBoardsNamesCache.Base(core.storage())
        ),
        CreateBoardCommunication.Base(),
        core.navigation(),
        core.provideDispatchersList()
    )
}