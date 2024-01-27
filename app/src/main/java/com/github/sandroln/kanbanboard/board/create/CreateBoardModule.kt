package com.github.sandroln.kanbanboard.board.create

import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.chosenboard.MyBoardsNamesCache
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.board.create.data.CreateBoardCloudDataSource
import com.github.sandroln.kanbanboard.board.create.data.CreateBoardRepository
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardCommunication
import com.github.sandroln.kanbanboard.board.create.presentation.CreateBoardViewModel
import com.github.sandroln.kanbanboard.core.CoreImpl

class CreateBoardModule(
    private val core: CoreImpl,
    private val boardScreenNavigation: BoardScreenNavigation
) : Module<CreateBoardViewModel> {

    override fun viewModel() = CreateBoardViewModel(
        BoardScreenNavigation.Base(core, boardScreenNavigation),
        core.manageResource(),
        CreateBoardRepository.Base(
            CreateBoardCloudDataSource.Base(core.provideMyUser(), core.service()),
            ChosenBoardCache.Base(core.storage()),
            MyBoardsNamesCache.Base(core.storage())
        ),
        CreateBoardCommunication.Base(),
        core.navigation(),
        core.provideDispatchersList()
    )
}