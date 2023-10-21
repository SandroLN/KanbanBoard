package com.github.sandroln.kanbanboard.ticket.edit

import com.github.sandroln.kanbanboard.board.main.data.Assignee
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.core.Core
import com.github.sandroln.kanbanboard.core.Module
import com.github.sandroln.kanbanboard.ticket.edit.data.ChangeTicketFields
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketCloudDataSource
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketRepository
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCommunication
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketStateCommunication
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketViewModel

class EditTicketModule(
    private val assigneeNameId: Assignee.NameAndId,
    private val core: Core,
    private val boardMembersCommunication: BoardMembersCommunication.Observe,
) : Module<EditTicketViewModel> {

    override fun viewModel(): EditTicketViewModel {
        val editTicketCommunication = EditTicketCommunication.Base()
        val editTicketIdCache = EditTicketIdCache.Base(core.storage())
        val cloudDataSource =
            EditTicketCloudDataSource.Base(
                assigneeNameId,
                editTicketCommunication,
                core,
                editTicketIdCache
            )
        return EditTicketViewModel(
            EditTicketStateCommunication.Base(),
            EditTicketRepository.Base(
                editTicketIdCache,
                ChangeTicketFields.Base(cloudDataSource, assigneeNameId),
                cloudDataSource
            ),
            boardMembersCommunication,
            editTicketCommunication,
            core.navigation()
        )
    }
}