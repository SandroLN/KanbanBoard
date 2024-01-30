package com.github.sandroln.kanbanboard.ticket.edit.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.openedboard.TicketUi
import com.github.sandroln.ticketcommon.TicketFragment
import com.google.android.material.textfield.TextInputEditText

class EditTicketFragment : TicketFragment<EditTicketViewModel>(R.layout.fragment_edit_ticket) {

    override val backButtonId = R.id.backButton
    override val actionButtonId = R.id.actionButton

    override val viewModelClass = EditTicketViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        val refreshButton = view.findViewById<View>(R.id.refreshButton)
        val deleteView = view.findViewById<DeleteTicketView>(R.id.deleteTicketView)
        val editTicketButton = view.findViewById<Button>(R.id.actionButton)
        val assigneeEditText = view.findViewById<TextInputEditText>(assigneeEditTextId)
        val membersRecyclerView = view.findViewById<RecyclerView>(boardMembersRecyclerViewId)
        //endregion

        refreshButton.setOnClickListener {
            viewModel.refresh()
        }

        deleteView.init(viewModel)

        val actions = object : EditingTicketActions {
            override fun deleteTicket() {
                Toast.makeText(
                    requireActivity(),
                    R.string.ticket_deleted,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.goBack()
            }

            override fun update(ticketUi: TicketUi) = viewModel.update(ticketUi)
        }

        editTicketButton.setOnClickListener {
            viewModel.edit(
                UiChangeList.Base(
                    titleEditText.text.toString(),
                    chooseColumnViewGroup.chosenColumn(),
                    colorsViewGroup.chosenColorHex(),
                    descriptionEditText.text.toString()
                )
            )
        }
        viewModel.observeTicketUiState(this) {
            it.show(refreshButton)
            it.show(
                titleEditText,
                chooseColumnViewGroup,
                colorsViewGroup,
                assigneeEditText,
                descriptionEditText,
                membersRecyclerView
            )
        }

        viewModel.observeTicketChanges(this) {
            it.realize(actions)
        }
        viewModel.init(savedInstanceState == null)
    }
}