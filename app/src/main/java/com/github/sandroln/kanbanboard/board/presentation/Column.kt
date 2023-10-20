package com.github.sandroln.kanbanboard.board.presentation

import android.view.View

interface Column {

    fun cloudValue(): String
    fun leftColumn(): Column
    fun rightColumn(): Column
    fun directionOfMove(targetColumn: Column, ticketUi: TicketUi): MoveDirection
    fun showButtons(left: View, right: View)

    object Todo : Column {

        override fun showButtons(left: View, right: View) {
            left.visibility = View.INVISIBLE
            right.visibility = View.VISIBLE
        }

        override fun cloudValue() = "todo"

        override fun leftColumn() = throw IllegalStateException("no more left columns from Todo")

        override fun rightColumn() = InProgress

        override fun directionOfMove(targetColumn: Column, ticketUi: TicketUi) =
            if (targetColumn == InProgress)
                MoveDirection.Right(ticketUi)
            else
                MoveDirection.None
    }

    object InProgress : Column {

        override fun cloudValue() = "inprogress"

        override fun showButtons(left: View, right: View) {
            left.visibility = View.VISIBLE
            right.visibility = View.VISIBLE
        }

        override fun leftColumn() = Todo

        override fun rightColumn() = Done

        override fun directionOfMove(targetColumn: Column, ticketUi: TicketUi) =
            when (targetColumn) {
                Done -> MoveDirection.Right(ticketUi)
                Todo -> MoveDirection.Left(ticketUi)
                else -> MoveDirection.None
            }
    }

    object Done : Column {

        override fun cloudValue() = "done"

        override fun showButtons(left: View, right: View) {
            left.visibility = View.VISIBLE
            right.visibility = View.INVISIBLE
        }

        override fun leftColumn() = InProgress

        override fun rightColumn() = throw IllegalStateException("no right column from Done")

        override fun directionOfMove(targetColumn: Column, ticketUi: TicketUi) =
            if (targetColumn == InProgress)
                MoveDirection.Left(ticketUi)
            else
                MoveDirection.None
    }
}