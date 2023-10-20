package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.kanbanboard.board.data.BoardUser
import com.github.sandroln.kanbanboard.board.data.TicketCloud
import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.ProvideDatabase

interface CreateTicketRepository {

    fun createTicket(createTicketBundle: CreateTicketBundle)

    class Base(
        private val chosenBoardIdCache: ChosenBoardCache.Read,
        private val provideDatabase: ProvideDatabase
    ) : CreateTicketRepository {

        override fun createTicket(createTicketBundle: CreateTicketBundle) {
            val board = chosenBoardIdCache.read()
            val ticket = board.createTicket(createTicketBundle)
            val reference = provideDatabase.database()
                .child("tickets")
                .push()
            reference.setValue(ticket)
        }
    }
}

interface CreateTicketOnBoard {

    fun createTicket(boardId: String): TicketCloud
}

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