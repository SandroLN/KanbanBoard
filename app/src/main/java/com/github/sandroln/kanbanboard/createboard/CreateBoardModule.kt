package com.github.sandroln.kanbanboard.createboard

import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.boards.data.MyBoardsNamesCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module
import com.github.sandroln.kanbanboard.createboard.data.CreateBoardCloudDataSource
import com.github.sandroln.kanbanboard.createboard.data.CreateBoardRepository
import com.github.sandroln.kanbanboard.createboard.presentation.CreateBoardCommunication
import com.github.sandroln.kanbanboard.createboard.presentation.CreateBoardViewModel

class CreateBoardModule(private val core: Core) : Module<CreateBoardViewModel> {

    override fun viewModel() = CreateBoardViewModel(
        core.manageResource(),
        CreateBoardRepository.Base(
            CreateBoardCloudDataSource.Base(core),
            ChosenBoardCache.Base(core.storage()),
            MyBoardsNamesCache.Base(core.storage())
        ),
        CreateBoardCommunication.Base(),
        core.navigation(),
        core.provideDispatchersList()
    )
}