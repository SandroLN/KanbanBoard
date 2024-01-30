package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.openedboard.TicketUi

interface EditTicketCallback {

    fun realize(actions: EditingTicketActions)

    class Update(private val ticketUi: TicketUi) : EditTicketCallback {
        override fun realize(actions: EditingTicketActions) = actions.update(ticketUi)
    }

    object Delete : EditTicketCallback {
        override fun realize(actions: EditingTicketActions) = actions.deleteTicket()
    }
}