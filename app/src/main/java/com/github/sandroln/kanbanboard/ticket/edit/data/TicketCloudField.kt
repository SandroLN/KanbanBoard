package com.github.sandroln.kanbanboard.ticket.edit.data

interface TicketCloudField {

    fun updateValue(changeField: ChangeSingleField, newValue: Any)

    abstract class Abstract(private val key: String) : TicketCloudField {
        override fun updateValue(changeField: ChangeSingleField, newValue: Any) =
            changeField.change(key, newValue)
    }

    object Title : Abstract("title")
    object Column : Abstract("columnId")
    object Description : Abstract("description")
    object Color : Abstract("color")
    object Assignee : Abstract("assignee")
}