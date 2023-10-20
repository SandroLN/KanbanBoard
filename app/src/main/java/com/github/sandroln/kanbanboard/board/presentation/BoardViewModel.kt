package com.github.sandroln.kanbanboard.board.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.github.sandroln.kanbanboard.board.data.BoardRepository
import com.github.sandroln.kanbanboard.core.Communication
import com.github.sandroln.kanbanboard.core.Reload
import com.github.sandroln.kanbanboard.core.Serialization
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.ticket.create.presentation.CreateTicketScreen

class BoardViewModel(
    private val serialization: Serialization.Mutable,
    private val repository: BoardRepository,
    private val ticketsCommunication: TicketsCommunication.Observe,
    private val communication: BoardCommunication,
    private val navigation: NavigationCommunication.Update,
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
        //todo navigation.map(EditTicketScreen)
    }

    override fun createTicket() = navigation.map(CreateTicketScreen)

    override fun toString(obj: Any) = serialization.toString(obj)

    override fun <T : Any> fromString(source: String, clasz: Class<T>): T =
        serialization.fromString(source, clasz)
}

interface BoardActions : Communication.Observe<BoardUiState>,
    TicketsCommunication.Observe, TicketInteractions, Reload, Serialization.Mutable {

    fun createTicket()
}

interface MoveDirection {

    fun move(moveTicket: MoveTickets<TicketUi>)

    object None : MoveDirection {
        override fun move(moveTicket: MoveTickets<TicketUi>) = Unit
    }

    class Left(private val ticketUi: TicketUi) : MoveDirection {
        override fun move(moveTicket: MoveTickets<TicketUi>) = moveTicket.moveLeft(ticketUi)
    }

    class Right(private val ticketUi: TicketUi) : MoveDirection {
        override fun move(moveTicket: MoveTickets<TicketUi>) = moveTicket.moveRight(ticketUi)
    }
}