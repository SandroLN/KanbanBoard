package com.github.sandroln.board.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.board.data.BoardRepository
import com.github.sandroln.core.Communication
import com.github.sandroln.core.Reload
import com.github.sandroln.core.Serialization
import com.github.sandroln.openedboard.Column
import com.github.sandroln.openedboard.TicketUi
import com.github.sandroln.openedboard.TicketUiSerializable

internal class BoardViewModel(
    private val serialization: Serialization.Mutable,
    private val repository: BoardRepository,
    private val ticketsCommunication: TicketsCommunication.Observe,
    private val communication: BoardCommunication,
    private val navigation: BoardToTicketNavigation
) : ViewModel(), BoardActions {

    init {
        repository.init()
    }

    override fun reload() = repository.init()

    //region observe
    override fun observe(owner: LifecycleOwner, observer: Observer<BoardUiState>) =
        communication.observe(owner, observer)

    override fun observeTodoColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>) =
        ticketsCommunication.observeTodoColumn(owner, observer)

    override fun observeInProgressColumn(
        owner: LifecycleOwner,
        observer: Observer<List<TicketUi>>
    ) = ticketsCommunication.observeInProgressColumn(owner, observer)

    override fun observeDoneColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>) =
        ticketsCommunication.observeDoneColumn(owner, observer)
    //endregion

    override fun moveLeft(moving: TicketUi) {
        communication.map(BoardUiState.ShowProgress)
        moving.moveLeft(repository)
    }

    override fun moveRight(moving: TicketUi) {
        communication.map(BoardUiState.ShowProgress)
        moving.moveRight(repository)
    }

    override fun change(adapterColumn: Column, item: String) {
        val ticketUi = fromString(item, TicketUiSerializable::class.java).toTicketUi()
        val directionOfMove = ticketUi.directionOfMove(adapterColumn)
        directionOfMove.move(this)
    }

    override fun showDetails(id: String) {
        repository.saveTicketIdToEdit(id)
        navigation.navigateToEditTicket()
    }

    override fun createTicket() = navigation.navigateToCreateTicket()

    override fun toString(obj: Any) = serialization.toString(obj)

    override fun <T : Any> fromString(source: String, clasz: Class<T>): T =
        serialization.fromString(source, clasz)
}

internal interface BoardActions : Communication.Observe<BoardUiState>,
    TicketsCommunication.Observe, TicketInteractions, Reload, Serialization.Mutable {

    fun createTicket()
}

interface BoardToTicketNavigation {
    fun navigateToCreateTicket()
    fun navigateToEditTicket()
}