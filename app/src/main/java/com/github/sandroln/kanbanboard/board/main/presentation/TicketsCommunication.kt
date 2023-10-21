package com.github.sandroln.kanbanboard.board.main.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

interface TicketsCommunication {

    interface Update {
        fun updateTodoTickets(ticketsUi: List<TicketUi>)
        fun updateInProgressTickets(ticketsUi: List<TicketUi>)
        fun updateDoneTickets(ticketsUi: List<TicketUi>)
    }

    interface Observe {
        fun observeTodoColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>)
        fun observeInProgressColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>)
        fun observeDoneColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>)
    }

    interface Mutable : Update, Observe

    class Base(
        private val todoTicketsCommunication: ColumnTicketCommunication,
        private val inProgressTicketsCommunication: ColumnTicketCommunication,
        private val doneTicketsCommunication: ColumnTicketCommunication,
    ) : Mutable {

        override fun updateTodoTickets(ticketsUi: List<TicketUi>) =
            todoTicketsCommunication.map(ticketsUi)

        override fun updateInProgressTickets(ticketsUi: List<TicketUi>) =
            inProgressTicketsCommunication.map(ticketsUi)

        override fun updateDoneTickets(ticketsUi: List<TicketUi>) =
            doneTicketsCommunication.map(ticketsUi)

        override fun observeTodoColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>) =
            todoTicketsCommunication.observe(owner, observer)

        override fun observeInProgressColumn(
            owner: LifecycleOwner,
            observer: Observer<List<TicketUi>>
        ) = inProgressTicketsCommunication.observe(owner, observer)

        override fun observeDoneColumn(owner: LifecycleOwner, observer: Observer<List<TicketUi>>) =
            doneTicketsCommunication.observe(owner, observer)
    }
}