package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.board.presentation.TicketUi

data class TicketCloud(
    val boardId: String = "",
    val columnId: String = Column.Todo.cloudValue(),
    val title: String = "",
    val assignee: String = "",
    val description: String = "",
    val color: String = TicketColor.Yellow.name
) {
    fun toUi(id: String, column: Column, assigneeName: AssigneeName) = TicketUi(
        TicketColor.Factory.find(color).value, id, title, assigneeName.name(assignee), column
    )
}

interface TicketColor {

    abstract class Abstract(val name: String, val value: String) : TicketColor

    object Red : Abstract("red", "#ef476f")
    object Orange : Abstract("orange", "#f77f00")
    object Purple : Abstract("purple", "#8f2d56")
    object Green : Abstract("green", "#06d6a0")
    object Blue : Abstract("blue", "#118ab2")
    object Yellow : Abstract("yellow", "#ffd166")

    object Factory {

        val list = listOf(Yellow, Orange, Red, Purple, Green, Blue)

        fun find(name: String) = list.find { it.name == name } ?: Yellow
    }
}