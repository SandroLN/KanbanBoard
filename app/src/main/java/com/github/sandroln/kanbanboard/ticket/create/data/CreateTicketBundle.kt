package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.main.data.TicketCloud
import com.github.sandroln.kanbanboard.board.main.presentation.Column

data class CreateTicketBundle(
    private val title: String,
    private val color: String,
    private val assignedUser: BoardUser,
    private val description: String
) : CreateTicketOnBoard {

    override fun createTicket(boardId: String): TicketCloud = TicketCloud(
        boardId,
        Column.Todo.cloudValue(),
        title,
        assignedUser.id(),
        description,
        color
    )
}