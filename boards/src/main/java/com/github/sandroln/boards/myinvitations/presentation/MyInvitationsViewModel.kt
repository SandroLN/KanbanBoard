package com.github.sandroln.boards.myinvitations.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.boards.myinvitations.data.MyInvitationsRepository
import com.github.sandroln.core.Communication

internal class MyInvitationsViewModel(
    private val repository: MyInvitationsRepository,
    private val communication: MyInvitationsCommunication
) : ViewModel(), InvitationActions, Communication.Observe<List<BoardInvitation>>,
    MyInvitationsRepository.Callback {

    init {
        repository.init(this)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<List<BoardInvitation>>) =
        communication.observe(owner, observer)

    override fun accept(id: String, boardId: String) = repository.accept(id, boardId)

    override fun decline(id: String) = repository.decline(id)

    override fun provideInvitations(list: List<BoardInvitation>) = communication.map(list)
}