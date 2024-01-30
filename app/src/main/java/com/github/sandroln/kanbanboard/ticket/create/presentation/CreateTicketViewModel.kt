package com.github.sandroln.kanbanboard.ticket.create.presentation

import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketUiBundle
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.ticketcommon.TicketViewModel

class CreateTicketViewModel(
    private val repository: CreateTicketRepository,
    boardMembersCommunication: BoardMembersCommunication.Observe,
    navigation: NavigationCommunication.Update
) : TicketViewModel(navigation, boardMembersCommunication) {

    fun createTicket(createTicketUiBundle: CreateTicketUiBundle) {
        createTicketUiBundle.assign(assignedUser)
        repository.createTicket(createTicketUiBundle)
        goBack()
    }
}