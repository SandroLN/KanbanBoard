package com.github.sandroln.kanbanboard.board.presentation

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.github.sandroln.kanbanboard.board.data.MoveTicket
import com.github.sandroln.kanbanboard.core.Communication

interface ColumnTicketCommunication : Communication.Mutable<List<TicketUi>> {
    class Base : Communication.Abstract<List<TicketUi>>(), ColumnTicketCommunication
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

    fun map(left: View, right: View) = when (column) {
        Column.TODO -> {
            left.visibility = View.INVISIBLE
            right.visibility = View.VISIBLE
        }

        Column.IN_PROGRESS -> {
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
        }

        Column.DONE -> {
            left.visibility = View.VISIBLE
            right.visibility = View.INVISIBLE
        }
    }

    fun directionOfMove(targetColumn: Column): MoveDirection = when (column) {
        Column.TODO -> if (targetColumn == Column.IN_PROGRESS)
            MoveDirection.RIGHT
        else
            MoveDirection.NONE

        Column.IN_PROGRESS -> when (targetColumn) {
            Column.DONE -> MoveDirection.RIGHT
            Column.TODO -> MoveDirection.LEFT
            else -> MoveDirection.NONE
        }

        Column.DONE -> if (targetColumn == Column.IN_PROGRESS)
            MoveDirection.LEFT
        else
            MoveDirection.NONE
    }

    override fun moveLeft(moving: MoveTicket) {
        val column = if (column == Column.DONE) Column.IN_PROGRESS else Column.TODO
        moving.moveTicket(id, column)
    }

    override fun moveRight(moving: MoveTicket) {
        val column = if (column == Column.TODO) Column.IN_PROGRESS else Column.DONE
        moving.moveTicket(id, column)
    }
}

enum class Column { //todo refactor
    TODO,
    IN_PROGRESS,
    DONE
}