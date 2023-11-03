package com.github.sandroln.kanbanboard.ticket.create

import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository
import com.github.sandroln.kanbanboard.ticket.create.presentation.CreateTicketViewModel

class CreateTicketModule(private val core: Core) : Module<CreateTicketViewModel> {

    override fun viewModel() = CreateTicketViewModel(
        CreateTicketRepository.Base(ChosenBoardCache.Base(core.storage()), core),
        core.boardScopeModule().provideMembers(),
        core.navigation()
    )
}