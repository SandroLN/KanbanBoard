package com.github.sandroln.kanbanboard.board.main.presentation

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.github.sandroln.kanbanboard.board.main.data.MoveTicket

data class TicketUi(
    private val colorHex: String,
    private val id: String,
    private val title: String,
    private val assignedMemberName: String,
    private val column: Column,
    private val description: String
) : MoveTickets<MoveTicket> {

    interface Mapper<T : Any> {
        fun map(
            colorHex: String,
            id: String,
            title: String,
            assignedMemberName: String,
            column: Column,
            description: String
        ): T
    }

    fun <T : Any> map(mapper: Mapper<T>): T = mapper.map(
        colorHex, id, title, assignedMemberName, column, description
    )

    fun show(): ShowTicket =
        ShowTicket.Base(column, colorHex, title, assignedMemberName, description)

    fun same(other: TicketUi) = id == other.id

    fun showDetails(interaction: ShowTicketDetails) = interaction.showDetails(id)

    fun map(colorView: View, title: TextView, assignee: TextView) {
        colorView.setBackgroundColor(Color.parseColor(colorHex))
        title.text = this.title
        assignee.text = assignedMemberName
    }

    fun map(left: View, right: View) = column.showButtons(left, right)

    fun directionOfMove(targetColumn: Column) = column.directionOfMove(targetColumn, this)

    override fun moveLeft(moving: MoveTicket) = moving.moveTicket(id, column.leftColumn())

    override fun moveRight(moving: MoveTicket) = moving.moveTicket(id, column.rightColumn())

    fun toSerializable() =
        TicketUiSerializable(
            colorHex,
            id,
            title,
            assignedMemberName,
            column.cloudValue(),
            description
        )
}

data class TicketUiSerializable(
    private val colorHex: String,
    private val id: String,
    private val name: String,
    private val assignedMemberName: String,
    private val columnCloudValue: String,
    private val description: String
) {

    fun toTicketUi(): TicketUi {
        val column = when (columnCloudValue) {
            Column.Todo.cloudValue() -> Column.Todo
            Column.InProgress.cloudValue() -> Column.InProgress
            Column.Done.cloudValue() -> Column.Done
            else -> throw IllegalStateException("unknown column")
        }
        return TicketUi(colorHex, id, name, assignedMemberName, column, description)
    }
}