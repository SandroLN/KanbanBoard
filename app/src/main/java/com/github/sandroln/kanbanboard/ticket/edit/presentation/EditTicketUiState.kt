package com.github.sandroln.kanbanboard.ticket.edit.presentation

import android.view.View
import android.widget.EditText
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser
import com.github.sandroln.kanbanboard.ticket.create.presentation.ColorsViewGroup
import com.google.android.material.textfield.TextInputEditText

interface EditTicketUiState {

    fun show(refreshButton: View)

    fun show(
        titleEditText: TextInputEditText,
        colorsViewGroup: ColorsViewGroup,
        assigneeEditText: TextInputEditText,
        descriptionEditText: TextInputEditText,
        membersRecyclerView: View
    ) = Unit

    abstract class Abstract(private val showRefreshButton: Int) : EditTicketUiState {

        override fun show(refreshButton: View) {
            refreshButton.visibility = showRefreshButton
        }
    }

    data class ShowTicketUpdate(
        private val ticketUi: TicketUi,
        private val assignUser: AssignUser
    ) : Abstract(View.GONE) {

        override fun show(
            titleEditText: TextInputEditText,
            colorsViewGroup: ColorsViewGroup,
            assigneeEditText: TextInputEditText,
            descriptionEditText: TextInputEditText,
            membersRecyclerView: View
        ) {
            val show = ticketUi.show()
            show.show(titleEditText, colorsViewGroup, assigneeEditText, descriptionEditText)
            show.show(assignUser)
            membersRecyclerView.visibility = View.GONE
        }
    }

    object ShowRefresh : Abstract(View.VISIBLE)
}

fun EditText.setTextCorrect(text: String) {
    setText(text)
    setSelection(text.length)
}