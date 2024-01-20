package com.github.sandroln.kanbanboard.ticket.create.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.ticket.common.presentation.ChooseColumnViewGroup
import com.github.sandroln.kanbanboard.ticket.common.presentation.ColorsViewGroup
import com.github.sandroln.kanbanboard.ticket.common.presentation.TicketFragment
import com.github.sandroln.kanbanboard.ticket.create.data.CreateTicketUiBundle
import com.google.android.material.textfield.TextInputEditText

class CreateTicketFragment :
    TicketFragment<CreateTicketViewModel>(R.layout.fragment_create_ticket) {

    override val viewModelClass = CreateTicketViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colorsViewGroup = view.findViewById<ColorsViewGroup>(R.id.colorsViewGroup)
        val titleEditText = view.findViewById<TextInputEditText>(R.id.createTicketEditText)
        val createTicketButton = view.findViewById<Button>(R.id.actionButton)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.descriptionEditText)
        val chooseColumnViewGroup =
            view.findViewById<ChooseColumnViewGroup>(R.id.chooseColumnViewGroup)
        chooseColumnViewGroup.init(Column.Todo)

        createTicketButton.setOnClickListener {
            viewModel.createTicket(
                CreateTicketUiBundle.Base(
                    chooseColumnViewGroup.chosenColumn(),
                    titleEditText.text.toString(),
                    colorsViewGroup.chosenColorName(),
                    descriptionEditText.text.toString()
                )
            )
        }
    }
}