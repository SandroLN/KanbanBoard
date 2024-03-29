package com.github.sandroln.boardssettings.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.boardssettings.data.BoardSettingsRepository
import com.github.sandroln.core.BaseViewModel
import com.github.sandroln.core.DispatchersList
import com.github.sandroln.core.GoBack
import com.github.sandroln.core.NavigationCommunication
import com.github.sandroln.core.Screen
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.openedboard.BoardUser

internal class BoardSettingsViewModel(
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

internal interface ObserveFoundUsers {
    fun observeFoundUsers(owner: LifecycleOwner, observer: Observer<List<BoardUser>>)
}

internal interface FindUsers {
    fun findUsers(mail: String)
}