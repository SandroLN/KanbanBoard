package com.github.sandroln.createticket

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Core
import com.github.sandroln.core.Module
import com.github.sandroln.createticket.data.CreateTicketRepository
import com.github.sandroln.createticket.presentation.CreateTicketViewModel
import com.github.sandroln.openedboard.ProvideBoardScopeModule

internal class CreateTicketModule(
    private val boardScopeModule: ProvideBoardScopeModule,
    private val core: Core
) : Module<CreateTicketViewModel> {

    override fun viewModel() = CreateTicketViewModel(
        CreateTicketRepository.Base(ChosenBoardCache.Base(core.storage()), core.service()),
        boardScopeModule.boardScopeModule().provideMembers(),
        core.navigation()
    )
}