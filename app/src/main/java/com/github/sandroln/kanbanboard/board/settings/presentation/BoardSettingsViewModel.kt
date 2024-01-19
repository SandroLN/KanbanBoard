package com.github.sandroln.kanbanboard.board.settings.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.settings.data.BoardSettingsRepository
import com.github.sandroln.kanbanboard.core.BaseViewModel
import com.github.sandroln.kanbanboard.core.DispatchersList
import com.github.sandroln.kanbanboard.core.GoBack
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.main.Screen
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser

class BoardSettingsViewModel(
    private val navigation: NavigationCommunication.Update,
    private val foundUsersCommunication: FoundUsersCommunication,
    dispatchersList: DispatchersList,
    private val boardSettingsRepository: BoardSettingsRepository,
    private val boardMembersCommunication: BoardMembersCommunication.Observe
) : BaseViewModel(dispatchersList), FindUsers, BoardMembersCommunication.Observe,
    ObserveFoundUsers, AssignUser, GoBack {

    override fun observeFoundUsers(owner: LifecycleOwner, observer: Observer<List<BoardUser>>) =
        foundUsersCommunication.observe(owner, observer)

    override fun observe(owner: LifecycleOwner, observer: Observer<List<BoardUser>>) =
        boardMembersCommunication.observe(owner, observer)

    override fun findUsers(mail: String) {
        if (mail.length < 3)
            foundUsersCommunication.map(emptyList())
        else {
            val findUsersAsync: suspend () -> List<BoardUser.Base> = {
                boardSettingsRepository.findUsers(mail)
                    .map { BoardUser.Base(it.first, it.second.name, it.second.mail) }
            }
            handle(findUsersAsync) {
                foundUsersCommunication.map(it)
            }
        }
    }

    override fun assign(user: BoardUser) = boardSettingsRepository.inviteUserToBoard(user)

    override fun goBack() = navigation.map(Screen.Pop)
}

interface ObserveFoundUsers {
    fun observeFoundUsers(owner: LifecycleOwner, observer: Observer<List<BoardUser>>)
}

interface FindUsers {
    fun findUsers(mail: String)
}