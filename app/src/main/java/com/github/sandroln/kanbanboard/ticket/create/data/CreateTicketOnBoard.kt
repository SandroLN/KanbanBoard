package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.openedboard.TicketCloud

interface CreateTicketOnBoard {

    fun createTicket(boardId: String): TicketCloud
}