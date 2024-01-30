package com.github.sandroln.editticket.presentation

import com.github.sandroln.openedboard.TicketUi

internal interface EditTicketCallback {

    fun realize(actions: EditingTicketActions)

    class Update(private val ticketUi: TicketUi) : EditTicketCallback {
        override fun realize(actions: EditingTicketActions) = actions.update(ticketUi)
    }

    object Delete : EditTicketCallback {
        override fun realize(actions: EditingTicketActions) = actions.deleteTicket()
    }
}