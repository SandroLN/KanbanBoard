package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache

interface CreateTicketRepository {

    fun createTicket(createTicketOnBoard: CreateTicketOnBoard)

    class Base(
        private val chosenBoardIdCache: ChosenBoardCache.Read,
        private val service: Service
    ) : CreateTicketRepository {

        override fun createTicket(createTicketOnBoard: CreateTicketOnBoard) {
            val board = chosenBoardIdCache.read()
            val ticket = board.createTicket(createTicketOnBoard)
            service.postFirstLevelAsync("tickets", ticket)
        }
    }
}