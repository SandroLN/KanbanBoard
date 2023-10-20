package com.github.sandroln.kanbanboard.ticket.create.presentation

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.board.data.BoardUser
import com.github.sandroln.kanbanboard.core.BaseFragment
import com.github.sandroln.kanbanboard.core.SimpleTextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class CreateTicketFragment : BaseFragment<CreateTicketViewModel>(R.layout.fragment_create_ticket) {

    override val viewModelClass = CreateTicketViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        val backButton = view.findViewById<View>(R.id.backButton)
        val colorsViewGroup = view.findViewById<ColorsViewGroup>(R.id.colorsViewGroup)
        val createTicketButton = view.findViewById<Button>(R.id.createTicketButton)
        val titleEditText = view.findViewById<TextInputEditText>(R.id.createTicketEditText)
        val assigneeInputLayout = view.findViewById<TextInputLayout>(R.id.assigneeInputLayout)
        val assigneeEditText = view.findViewById<TextInputEditText>(R.id.assigneeEditText)
        val membersRecyclerView = view.findViewById<RecyclerView>(R.id.boardMembersRecyclerView)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.descriptionEditText)
        //endregion
        val assignUser = object : AssignUser {
            override fun assign(user: BoardUser) {
                viewModel.assign(user)
                assigneeEditText.setText(user.name())
                assigneeEditText.setSelection(user.name().length)
                membersRecyclerView.visibility = View.GONE
            }
        }
        val membersAdapter = MembersAdapter(assignUser)
        membersRecyclerView.adapter = membersAdapter
        assigneeEditText.setOnFocusChangeListener { _, hasFocus ->
            membersRecyclerView.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }
        assigneeEditText.addTextChangedListener(SearchBoardMember(membersAdapter) {
            membersRecyclerView.visibility = View.VISIBLE
        })
        assigneeInputLayout.setEndIconOnClickListener {
            assignUser.assign(BoardUser.None)
        }
        titleEditText.addTextChangedListener(CreateTicketTextWatcher {
            createTicketButton.isEnabled = it
        })
        createTicketButton.setOnClickListener {
            viewModel.createTicket(
                titleEditText.text.toString(),
                colorsViewGroup.chosenColorName(),
                descriptionEditText.text.toString()
            )
        }
        backButton.setOnClickListener {
            viewModel.goBack()
        }
        viewModel.observe(this) {
            membersAdapter.updateMembers(it)
        }
    }
}

private class SearchBoardMember(
    private val updateQuery: UpdateQuery,
    private val afterTextChanged: () -> Unit
) : SimpleTextWatcher() {

    override fun afterTextChanged(s: Editable) {
        updateQuery.updateQuery(s.toString())
        afterTextChanged.invoke()
    }
}

private class CreateTicketTextWatcher(
    private val enableCreateButton: (Boolean) -> Unit
) : SimpleTextWatcher() {

    override fun afterTextChanged(s: Editable) {
        val text = s.toString()
        enableCreateButton.invoke(text.length >= minimumLength)
    }

    companion object {
        private const val minimumLength: Int = 3
    }
}