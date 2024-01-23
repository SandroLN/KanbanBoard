package com.github.sandroln.kanbanboard.ticket.edit

import com.github.sandroln.core.Module
import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.core.CoreImpl
import com.github.sandroln.kanbanboard.ticket.edit.data.ChangeTicketFields
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketCloudDataSource
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketRepository
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCommunication
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketStateCommunication
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketViewModel

class EditTicketModule(private val core: CoreImpl) :
    Module<EditTicketViewModel> {

    override fun viewModel(): EditTicketViewModel {
        val editTicketCommunication = EditTicketCommunication.Base()
        val editTicketIdCache = EditTicketIdCache.Base(core.storage())
        val boardScopeModule = core.boardScopeModule()
        val assigneeNameId = boardScopeModule.provideContainer()
        val cloudDataSource =
            EditTicketCloudDataSource.Base(
                assigneeNameId,
                editTicketCommunication,
                core.service(),
                editTicketIdCache
            )
        return EditTicketViewModel(
            EditTicketStateCommunication.Base(),
            EditTicketRepository.Base(
                editTicketIdCache,
                ChangeTicketFields.Base(cloudDataSource, assigneeNameId),
                cloudDataSource
            ),
            boardScopeModule.provideMembers(),
            editTicketCommunication,
            core.navigation()
        )
    }
}