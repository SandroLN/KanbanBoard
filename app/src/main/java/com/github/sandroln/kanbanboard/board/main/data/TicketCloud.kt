package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.openedboard.Assignee

data class TicketCloud(
    val boardId: String = "",
    val columnId: String = "",
    val title: String = "",
    val assignee: String = "",
    val description: String = "",
    val color: String = ""
) {
    fun toUi(id: String, column: Column, assigneeName: Assignee.Name) = TicketUi(
        TicketColor.Factory.valueByName(color),
        id,
        title,
        assigneeName.name(assignee),
        column,
        description
    )

    fun toUi(id: String, assigneeName: Assignee.Name): TicketUi {
        val column = when (columnId) {
            Column.InProgress.cloudValue() -> Column.InProgress
            Column.Done.cloudValue() -> Column.Done
            else -> Column.Todo
        }
        return toUi(id, column, assigneeName)
    }
}