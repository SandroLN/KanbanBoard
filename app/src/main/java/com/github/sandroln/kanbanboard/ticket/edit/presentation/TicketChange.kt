package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.board.main.data.TicketColor
import com.github.sandroln.kanbanboard.ticket.edit.data.ChangeTicketFields

interface TicketChange {

    fun applyChanges(changeTicketFields: ChangeTicketFields)

    data class Title(private val value: String) : TicketChange {
        override fun applyChanges(changeTicketFields: ChangeTicketFields) =
            changeTicketFields.changeTitle(value)
    }

    data class Assignee(private val name: String) : TicketChange {
        override fun applyChanges(changeTicketFields: ChangeTicketFields) =
            changeTicketFields.changeAssignee(name)
    }

    data class Color(private val hex: String) : TicketChange {
        override fun applyChanges(changeTicketFields: ChangeTicketFields) {
            val colorName = TicketColor.Factory.nameByValue(hex)
            changeTicketFields.changeColor(colorName)
        }
    }

    data class Description(private val value: String) : TicketChange {
        override fun applyChanges(changeTicketFields: ChangeTicketFields) =
            changeTicketFields.changeDescription(value)
    }
}