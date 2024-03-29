package com.github.sandroln.board.data

import com.github.sandroln.board.presentation.BoardCommunication
import com.github.sandroln.board.presentation.BoardUiState
import com.github.sandroln.board.presentation.TicketsCommunication
import com.github.sandroln.openedboard.Assignee
import com.github.sandroln.openedboard.Column
import com.github.sandroln.openedboard.TicketCloud

internal interface UpdateBoard {

    fun update(tickets: List<Pair<String, TicketCloud>>, assigneeName: Assignee.Name)

    class Base(
        private val boardCommunication: BoardCommunication,
        private val ticketsCommunication: TicketsCommunication.Update
    ) : UpdateBoard {

        override fun update(tickets: List<Pair<String, TicketCloud>>, assigneeName: Assignee.Name) =
            with(ticketsCommunication) {
                boardCommunication.map(BoardUiState.HideProgress)
                updateTodoTickets(data(tickets, Column.Todo, assigneeName))
                updateInProgressTickets(data(tickets, Column.InProgress, assigneeName))
                updateDoneTickets(data(tickets, Column.Done, assigneeName))
            }

        private fun data(
            data: List<Pair<String, TicketCloud>>, column: Column, assigneeName: Assignee.Name
        ) = data.filter { it.second.columnId == column.cloudValue() }.map { (id, cloud) ->
            cloud.toUi(id, column, assigneeName)
        }
    }
}