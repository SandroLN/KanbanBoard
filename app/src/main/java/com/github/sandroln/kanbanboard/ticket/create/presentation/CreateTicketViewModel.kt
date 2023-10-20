package com.github.sandroln.kanbanboard.ticket.create.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.board.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.data.BoardUser
import com.github.sandroln.kanbanboard.core.GoBack
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketBundle
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketRepository

class CreateTicketViewModel(
    private val repository: CreateTicketRepository,
    private val boardMembersCommunication: BoardMembersCommunication.Mutable,
    private val navigation: NavigationCommunication.Update
) : ViewModel(), GoBack, BoardMembersCommunication.Observe, AssignUser {

    override fun observe(owner: LifecycleOwner, observer: Observer<List<BoardUser>>) =
        boardMembersCommunication.observe(owner, observer)

    override fun goBack() = navigation.map(Screen.Pop)

    private var assignedUser: BoardUser = BoardUser.None

    override fun assign(user: BoardUser) {
        assignedUser = user
    }

    fun createTicket(title: String, colorName: String, description: String) {
        repository.createTicket(CreateTicketBundle(title, colorName, assignedUser, description))
        goBack()
    }
}

interface AssignUser {
    fun assign(user: BoardUser)
}