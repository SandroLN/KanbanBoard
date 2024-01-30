package com.github.sandroln.kanbanboard.ticket.edit.presentation

import com.github.sandroln.kanbanboard.ticket.edit.data.ChangeTicketFields
import com.github.sandroln.openedboard.TicketColor

interface TicketChange {

    fun applyChanges(changeTicketFields: ChangeTicketFields)

    data class Title(private val value: String) : TicketChange {
        override fun applyChanges(changeTicketFields: ChangeTicketFields) =
            changeTicketFields.changeTitle(value)
    }

    data class Column(
        private val value: com.github.sandroln.openedboard.Column
    ) : TicketChange {

        override fun applyChanges(changeTicketFields: ChangeTicketFields) {
            changeTicketFields.changeColumn(value.cloudValue())
        }
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