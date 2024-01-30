package com.github.sandroln.createticket.data

import com.github.sandroln.chosenboard.BoardCache
import com.github.sandroln.chosenboard.ChosenBoardCache
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.openedboard.TicketCloud

internal interface CreateTicketRepository {

    fun createTicket(createTicketOnBoard: CreateTicketOnBoard)

    class Base(
        private val chosenBoardIdCache: ChosenBoardCache.Read,
        private val service: Service
    ) : CreateTicketRepository {

        override fun createTicket(createTicketOnBoard: CreateTicketOnBoard) {
            val board = chosenBoardIdCache.read()
            val ticket = board.map(CreateTicketMapper(createTicketOnBoard))
            service.postFirstLevelAsync("tickets", ticket)
        }
    }
}

private class CreateTicketMapper(
    private val createTicketOnBoard: CreateTicketOnBoard
) : BoardCache.Mapper<TicketCloud> {

    override fun map(id: String, name: String, isMyBoard: Boolean, ownerId: String) =
        createTicketOnBoard.createTicket(id)
}