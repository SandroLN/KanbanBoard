package com.github.sandroln.createticket.data

import com.github.sandroln.openedboard.TicketCloud

internal interface CreateTicketOnBoard {

    fun createTicket(boardId: String): TicketCloud
}