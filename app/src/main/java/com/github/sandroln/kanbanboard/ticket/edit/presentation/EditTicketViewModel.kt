package com.github.sandroln.kanbanboard.ticket.edit.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.github.sandroln.kanbanboard.board.main.data.BoardMembersCommunication
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.kanbanboard.core.Init
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.ticket.common.presentation.TicketViewModel
import com.github.sandroln.kanbanboard.ticket.edit.data.EditTicketRepository

class EditTicketViewModel(
    private val stateCommunication: EditTicketStateCommunication,
    private val repository: EditTicketRepository,
    boardMembersCommunication: BoardMembersCommunication.Observe,
    private val communication: EditTicketCommunication.Observe,
    navigation: NavigationCommunication.Update
) : TicketViewModel(navigation, boardMembersCommunication), EditTicketUiActions {

    override fun observeTicketUiState(
        owner: LifecycleOwner,
        observer: Observer<EditTicketUiState>
    ) = stateCommunication.observe(owner, observer)

    override fun observeTicketChanges(
        owner: LifecycleOwner,
        observer: Observer<EditTicketCallback>
    ) = communication.observe(owner, observer)

    override fun deleteTicket() {
        repository.deleteTicket()
        goBack()
    }

    override fun goBack() {
        repository.clearTicketId()
        super.goBack()
    }

    private val ticketUiMemento = mutableListOf<TicketUi>()

    override fun update(ticketUi: TicketUi) {
        with(ticketUiMemento) {
            if (isEmpty()) {
                add(ticketUi)
                stateCommunication.map(
                    EditTicketUiState.ShowTicketUpdate(
                        ticketUi,
                        this@EditTicketViewModel
                    )
                )
            } else {
                stateCommunication.map(EditTicketUiState.ShowRefresh)
                if (size == 1)
                    add(ticketUi)
                else
                    set(1, ticketUi)
            }
        }
    }

    fun refresh() =
        stateCommunication.map(EditTicketUiState.ShowTicketUpdate(ticketUiMemento.last(), this))

    override fun edit(title: String, colorHex: String, description: String) {
        val ticketUi = ticketUiMemento.last()
        val changes = ticketUi.checkChanges(
            title,
            colorHex,
            assignedUser.name(),
            description
        )

        if (changes.isNotEmpty())
            repository.makeChanges(changes)
        goBack()
    }

    override fun init(firstRun: Boolean) {
        if (firstRun) repository.init()
    }
}

interface EditTicketUiActions : Init, DeleteTicket, ObserveTicketChanges, UpdateTicketUi,
    ObserveTicketUiState {

    fun edit(title: String, colorHex: String, description: String)
}

interface ObserveTicketUiState {
    fun observeTicketUiState(owner: LifecycleOwner, observer: Observer<EditTicketUiState>) = Unit
}