package com.github.sandroln.ticketcommon

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.core.GoBack
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.Screen
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.openedboard.BoardUser


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

interface CommonActions : AssignUser, GoBack, BoardMembersCommunication.Observe