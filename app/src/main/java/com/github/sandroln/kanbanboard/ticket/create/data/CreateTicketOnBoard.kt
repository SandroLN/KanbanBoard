package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.kanbanboard.board.main.data.TicketCloud

interface CreateTicketOnBoard {

    fun createTicket(boardId: String): TicketCloud
}