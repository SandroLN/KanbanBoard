package com.github.sandroln.createboard

import com.github.sandroln.chosenboard.BoardCore
import com.github.sandroln.chosenboard.BoardScreenNavigation
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.chosenboard.MyBoardsNamesCache
import com.github.sandroln.common.NavigateToBoards
import com.github.sandroln.core.Module
import com.github.sandroln.createboard.data.CreateBoardCloudDataSource
import com.github.sandroln.createboard.data.CreateBoardRepository
import com.github.sandroln.createboard.presentation.CreateBoardCommunication
import com.github.sandroln.createboard.presentation.CreateBoardViewModel

internal class CreateBoardModule(
    private val core: BoardCore,
    private val navigation: CreateBoardNavigationList
) : Module<CreateBoardViewModel> {

    override fun viewModel() = CreateBoardViewModel(
        BoardScreenNavigation.Base(core, navigation),
        core.manageResource(),
        CreateBoardRepository.Base(
            CreateBoardCloudDataSource.Base(core.provideMyUser(), core.service()),
            ChosenBoardCache.Base(core.storage()),
            MyBoardsNamesCache.Base(core.storage())
        ),
        CreateBoardCommunication.Base(),
        navigation,
        core.provideDispatchersList()
    )
}

interface CreateBoardNavigationList : BoardScreenNavigation, NavigateToBoards