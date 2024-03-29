package com.github.sandroln.editticket.presentation

import android.view.View
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.TicketUi
import com.google.android.material.textfield.TextInputEditText


internal interface EditTicketUiState {

    fun show(refreshButton: View)

    fun show(
        titleEditText: TextInputEditText,
        chooseColumnViewGroup: com.github.sandroln.ticketcommon.ChooseColumnViewGroup,
        colorsViewGroup: com.github.sandroln.ticketcommon.ColorsViewGroup,
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
            chooseColumnViewGroup: com.github.sandroln.ticketcommon.ChooseColumnViewGroup,
            colorsViewGroup: com.github.sandroln.ticketcommon.ColorsViewGroup,
            assigneeEditText: TextInputEditText,
            descriptionEditText: TextInputEditText,
            membersRecyclerView: View
        ) {
            val show = ticketUi.show()
            show.show(
                titleEditText,
                chooseColumnViewGroup,
                colorsViewGroup,
                assigneeEditText,
                descriptionEditText
            )
            show.show(assignUser)
            membersRecyclerView.visibility = View.GONE
        }
    }

    object ShowRefresh : Abstract(View.VISIBLE)
}