package com.github.sandroln.kanbanboard.board.settings.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.board.settings.data.BoardInvitationRepository
import com.github.sandroln.openedboard.BoardMembersCommunication
import com.github.sandroln.openedboard.BoardUser

class BoardInvitationViewModel(
    repository: BoardInvitationRepository,
    private val communication: BoardMembersCommunication.Mutable
) : ViewModel(), BoardMembersCommunication.Observe, BoardInvitationRepository.Callback {

    init {
        repository.init(this)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<List<BoardUser>>) =
        communication.observe(owner, observer)

    override fun provideInvitations(users: List<BoardUser>) = communication.map(users)
}