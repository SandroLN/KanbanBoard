package com.github.sandroln.kanbanboard.ticket.edit.data

interface ChangeSingleField {

    fun change(key: String, newValue: Any)
}