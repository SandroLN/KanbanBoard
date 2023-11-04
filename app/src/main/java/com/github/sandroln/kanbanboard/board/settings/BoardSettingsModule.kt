package com.github.sandroln.kanbanboard.board.settings

import com.github.sandroln.kanbanboard.board.settings.data.BoardSettingsRepository
import com.github.sandroln.kanbanboard.board.settings.presentation.BoardSettingsViewModel
import com.github.sandroln.kanbanboard.board.settings.presentation.FoundUsersCommunication
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module

class BoardSettingsModule(private val core: Core) : Module<BoardSettingsViewModel> {

    override fun viewModel() = BoardSettingsViewModel(
        core.navigation(),
        FoundUsersCommunication.Base(),
        core.provideDispatchersList(),
        BoardSettingsRepository.Base(
            core.provideMyUser(),
            ChosenBoardCache.Base(core.storage()),
            core
        ),
        core.boardScopeModule().provideMembers()
    )
}