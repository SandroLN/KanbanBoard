package com.github.sandroln.boardssettings

import com.github.sandroln.boardssettings.data.BoardSettingsRepository
import com.github.sandroln.boardssettings.presentation.BoardSettingsViewModel
import com.github.sandroln.boardssettings.presentation.FoundUsersCommunication
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.openedboard.ProvideBoardScopeModule

internal class BoardSettingsModule(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core
) : Module<BoardSettingsViewModel> {

    override fun viewModel() = BoardSettingsViewModel(
        core.navigation(),
        FoundUsersCommunication.Base(),
        core.provideDispatchersList(),
        BoardSettingsRepository.Base(
            core.provideMyUser(),
            ChosenBoardCache.Base(core.storage()),
            core.service()
        ),
        boardScopeModule.boardScopeModule().provideMembers()
    )
}