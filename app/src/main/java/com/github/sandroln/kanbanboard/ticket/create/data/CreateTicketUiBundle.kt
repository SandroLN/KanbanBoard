package com.github.sandroln.kanbanboard.ticket.create.data

import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser
import com.github.sandroln.openedboard.BoardUser
import com.github.sandroln.openedboard.Column
import com.github.sandroln.openedboard.TicketCloud

interface CreateTicketUiBundle : CreateTicketOnBoard, AssignUser {

    class Base(
        private val column: Column,
        private val title: String,
        private val color: String,
        private val description: String
    ) : CreateTicketUiBundle {

        private var assignedUser: BoardUser = BoardUser.None

        override fun assign(user: BoardUser) {
            assignedUser = user
        }

        override fun createTicket(boardId: String): TicketCloud = TicketCloud(
            boardId,
            column.cloudValue(),
            title,
            assignedUser.id(),
            description,
            color
        )
    }
}