package com.github.sandroln.kanbanboard.board.settings

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.board.settings.data.BoardSettingsRepository
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardSettingsViewModel
import com.github.sandroln.kanbanboard.board.settings.presentation.FoundUsersCommunication
import com.github.sandroln.kanbanboard.core.CoreImpl

class BoardSettingsModule(private val core: CoreImpl) :
    Module<BoardSettingsViewModel> {

    override fun viewModel() = BoardSettingsViewModel(
        core.navigation(),
        FoundUsersCommunication.Base(),
        core.provideDispatchersList(),
        BoardSettingsRepository.Base(
            core.provideMyUser(),
            ChosenBoardCache.Base(core.storage()),
            core.service()
        ),
        core.boardScopeModule().provideMembers()
    )
}