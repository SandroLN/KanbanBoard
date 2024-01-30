package com.github.sandroln.createticket.presentation

import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.createticket.data.CreateTicketRepository
import com.github.sandroln.createticket.data.CreateTicketUiBundle
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.ticketcommon.TicketViewModel

internal class CreateTicketViewModel(
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