package com.github.sandroln.createticket.presentation

import android.os.Bundle
import android.view.View
import android.widget.Button
import com.github.sandroln.createticket.R
import com.github.sandroln.createticket.data.CreateTicketUiBundle
import com.github.sandroln.openedboard.Column
import com.github.sandroln.ticketcommon.TicketFragment

internal class CreateTicketFragment :
    TicketFragment<CreateTicketViewModel>(R.layout.fragment_create_ticket) {

    override val backButtonId = R.id.backButton
    override val actionButtonId = R.id.actionButton

    override val viewModelClass = CreateTicketViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val createTicketButton = view.findViewById<Button>(R.id.actionButton)
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