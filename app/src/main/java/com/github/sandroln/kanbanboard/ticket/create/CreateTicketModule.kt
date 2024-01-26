package com.github.sandroln.kanbanboard.ticket.create

import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.core.CoreImpl
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository
import com.github.sandroln.kanbanboard.ticket.create.presentation.CreateTicketViewModel

class CreateTicketModule(private val core: CoreImpl) :
    Module<CreateTicketViewModel> {

    override fun viewModel() = CreateTicketViewModel(
        CreateTicketRepository.Base(ChosenBoardCache.Base(core.storage()), core.service()),
        core.boardScopeModule().provideMembers(),
        core.navigation()
    )
}