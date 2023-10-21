package com.github.sandroln.kanbanboard.ticket.edit.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.main.presentation.TicketUi
import com.github.sandroln.kanbanboard.ticket.common.presentation.TicketFragment
import com.github.sandroln.kanbanboard.ticket.create.presentation.ColorsViewGroup
import com.google.android.material.textfield.TextInputEditText

class EditTicketFragment : TicketFragment<EditTicketViewModel>(R.layout.fragment_edit_ticket) {

    override val viewModelClass = EditTicketViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        val refreshButton = view.findViewById<View>(R.id.refreshButton)
        val deleteView = view.findViewById<DeleteTicketView>(R.id.deleteTicketView)
        val colorsViewGroup = view.findViewById<ColorsViewGroup>(R.id.colorsViewGroup)
        val editTicketButton = view.findViewById<Button>(R.id.actionButton)
        val titleEditText = view.findViewById<TextInputEditText>(R.id.createTicketEditText)
        val assigneeEditText = view.findViewById<TextInputEditText>(R.id.assigneeEditText)
        val membersRecyclerView = view.findViewById<RecyclerView>(R.id.boardMembersRecyclerView)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.descriptionEditText)
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
                titleEditText.text.toString(),
                colorsViewGroup.chosenColorHex(),
                descriptionEditText.text.toString()
            )
        }
        viewModel.observeTicketUiState(this) {
            it.show(refreshButton)
            it.show(
                titleEditText,
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