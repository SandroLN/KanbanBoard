package com.github.sandroln.kanbanboard.ticket.create.presentation

import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.kanbanboard.ticket.common.presentation.TicketViewModel
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketUiBundle
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.openedboard.BoardUser

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

interface AssignUser {
    fun assign(user: BoardUser)

    object Empty : AssignUser {
        override fun assign(user: BoardUser) = Unit
    }
}