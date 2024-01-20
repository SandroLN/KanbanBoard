package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser

interface UiChangeList : TicketUi.Mapper<List<TicketChange>>, AssignUser {

    class Base(
        private val otherTitle: String,
        private val otherColumn: Column,
        private val otherColor: String,
        private val otherDescription: String
    ) : UiChangeList {

        private var user: BoardUser = BoardUser.None

        override fun map(
            colorHex: String,
            id: String,
            title: String,
            assignedMemberName: String,
            column: Column,
            description: String
        ): List<TicketChange> {
            val list = mutableListOf<TicketChange>()

            if (title != otherTitle)
                list.add(TicketChange.Title(otherTitle))
            if (column != otherColumn)
                list.add(TicketChange.Column(otherColumn))
            if (otherColor != colorHex)
                list.add(TicketChange.Color(otherColor))
            if (assignedMemberName != user.name())
                list.add(TicketChange.Assignee(user.name()))
            if (description != otherDescription)
                list.add(TicketChange.Description(otherDescription))

            return list
        }

        override fun assign(user: BoardUser) {
            this.user = user
        }
    }
}