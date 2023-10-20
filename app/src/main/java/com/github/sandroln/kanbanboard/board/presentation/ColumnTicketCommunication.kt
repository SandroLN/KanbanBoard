package com.github.sandroln.kanbanboard.board.presentation

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.github.sandroln.kanbanboard.board.data.MoveTicket
import com.github.sandroln.kanbanboard.core.Communication

interface ColumnTicketCommunication : Communication.Mutable<List<TicketUi>> {
    class Base : Communication.Regular<List<TicketUi>>(), ColumnTicketCommunication
}

data class TicketUi(
    private val colorHex: String,
    private val id: String,
    private val name: String,
    private val assignedMemberName: String,
    private val column: Column
) : MoveTickets<MoveTicket> {

    fun same(other: TicketUi) = id == other.id

    fun showDetails(interaction: ShowTicketDetails) = interaction.showDetails(id)

    fun map(colorView: View, title: TextView, assignee: TextView) {
        colorView.setBackgroundColor(Color.parseColor(colorHex))
        title.text = name
        assignee.text = assignedMemberName
    }

    fun map(left: View, right: View) = column.showButtons(left, right)

    fun directionOfMove(targetColumn: Column) = column.directionOfMove(targetColumn, this)

    override fun moveLeft(moving: MoveTicket) = moving.moveTicket(id, column.leftColumn())

    override fun moveRight(moving: MoveTicket) = moving.moveTicket(id, column.rightColumn())

    fun toSerializable() =
        TicketUiSerializable(colorHex, id, name, assignedMemberName, column.cloudValue())
}

data class TicketUiSerializable(
    private val colorHex: String,
    private val id: String,
    private val name: String,
    private val assignedMemberName: String,
    private val columnCloudValue: String
) {

    fun toTicketUi(): TicketUi {
        val column = when (columnCloudValue) {
            Column.Todo.cloudValue() -> Column.Todo
            Column.InProgress.cloudValue() -> Column.InProgress
            Column.Done.cloudValue() -> Column.Done
            else -> throw IllegalStateException("unknown column")
        }
        return TicketUi(colorHex, id, name, assignedMemberName, column)
    }
}