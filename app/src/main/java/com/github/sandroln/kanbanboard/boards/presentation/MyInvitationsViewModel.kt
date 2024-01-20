package com.github.sandroln.kanbanboard.boards.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.boards.data.MyInvitationsRepository
import com.github.sandroln.kanbanboard.core.Communication

class MyInvitationsViewModel(
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