package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.board.presentation.TicketUi

data class TicketCloud(
    val boardId: String = "",
    val columnId: String = "todo",
    val title: String = "",
    val assignee: String = "",
    val description: String = "",
    val color: String = "#EBC944"
) {
    fun toUi(id: String, column: Column, assigneeName: AssigneeName) = TicketUi(
        color, id, title, assigneeName.name(assignee), column
    )
}