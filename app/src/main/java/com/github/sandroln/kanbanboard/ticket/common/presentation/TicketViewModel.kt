package com.github.sandroln.kanbanboard.ticket.common.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.Screen
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser

abstract class TicketViewModel(
    private val navigation: NavigationCommunication.Update,
    private val boardMembersCommunication: BoardMembersCommunication.Observe,
) : ViewModel(), CommonActions {

    override fun observe(owner: LifecycleOwner, observer: Observer<List<BoardUser>>) =
        boardMembersCommunication.observe(owner, observer)

    override fun goBack() = navigation.map(Screen.Pop)

    protected var assignedUser: BoardUser = BoardUser.None

    override fun assign(user: BoardUser) {
        assignedUser = user
    }
}

interface CommonActions : AssignUser, com.github.sandroln.core.GoBack, BoardMembersCommunication.Observe