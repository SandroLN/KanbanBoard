package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.ProvideDatabase

interface CreateTicketRepository {

    fun createTicket(createTicketOnBoard: CreateTicketOnBoard)

    class Base(
        private val chosenBoardIdCache: ChosenBoardCache.Read,
        private val provideDatabase: ProvideDatabase
    ) : CreateTicketRepository {

        override fun createTicket(createTicketOnBoard: CreateTicketOnBoard) {
            val board = chosenBoardIdCache.read()
            val ticket = board.createTicket(createTicketOnBoard)
            val reference = provideDatabase.database()
                .child("tickets")
                .push()
            reference.setValue(ticket)
        }
    }
}