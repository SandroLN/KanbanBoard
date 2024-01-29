package com.github.sandroln.kanbanboard.ticket.edit.data

import com.github.sandroln.openedboard.Assignee

interface ChangeTicketFields {

    fun changeTitle(value: String)

    fun changeColumn(value: String)

    fun changeDescription(value: String)

    fun changeColor(value: String)

    fun changeAssignee(name: String)

    class Base(
        private val changeField: ChangeSingleField,
        private val assigneeId: Assignee.Id
    ) : ChangeTicketFields {

        override fun changeTitle(value: String) =
            TicketCloudField.Title.updateValue(changeField, value)

        override fun changeColumn(value: String) =
            TicketCloudField.Column.updateValue(changeField, value)

        override fun changeDescription(value: String) =
            TicketCloudField.Description.updateValue(changeField, value)

        override fun changeColor(value: String) =
            TicketCloudField.Color.updateValue(changeField, value)

        override fun changeAssignee(name: String) =
            TicketCloudField.Assignee.updateValue(changeField, assigneeId.id(name))
    }
}