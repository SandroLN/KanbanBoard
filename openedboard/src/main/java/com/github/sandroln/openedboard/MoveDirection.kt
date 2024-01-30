package com.github.sandroln.openedboard

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