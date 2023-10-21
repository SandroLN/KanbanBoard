package com.github.sandroln.kanbanboard.ticket.create.presentation

import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.ticket.common.presentation.TicketViewModel
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketBundle
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository

class CreateTicketViewModel(
    private val repository: CreateTicketRepository,
    boardMembersCommunication: BoardMembersCommunication.Observe,
    navigation: NavigationCommunication.Update
) : TicketViewModel(navigation, boardMembersCommunication) {

    fun createTicket(title: String, colorName: String, description: String) {
        repository.createTicket(CreateTicketBundle(title, colorName, assignedUser, description))
        goBack()
    }
}

interface AssignUser {
    fun assign(user: BoardUser)
}