package com.github.sandroln.kanbanboard.ticket.common.presentation

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.ticket.create.presentation.AssignUser
import com.github.sandroln.kanbanboard.ticket.edit.presentation.setTextCorrect
import com.github.sandroln.openedboard.BoardUser
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class TicketFragment<T : TicketViewModel>(layoutId: Int) : com.github.sandroln.core.BaseFragment<T>(layoutId) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        val backButton = view.findViewById<View>(R.id.backButton)
        val actionButton = view.findViewById<Button>(R.id.actionButton)
        val titleEditText = view.findViewById<TextInputEditText>(R.id.createTicketEditText)
        val assigneeInputLayout = view.findViewById<TextInputLayout>(R.id.assigneeInputLayout)
        val assigneeEditText = view.findViewById<TextInputEditText>(R.id.assigneeEditText)
        val membersRecyclerView = view.findViewById<RecyclerView>(R.id.boardMembersRecyclerView)
        //endregion

        val assignUser = object : AssignUser {
            override fun assign(user: BoardUser) {
                viewModel.assign(user)
                assigneeEditText.setTextCorrect(user.name())
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
            actionButton.isEnabled = it
        })

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
) : com.github.sandroln.core.SimpleTextWatcher() {

    override fun afterTextChanged(s: Editable) {
        updateQuery.updateQuery(s.toString())
        afterTextChanged.invoke()
    }
}

private class CreateTicketTextWatcher(
    private val enableCreateButton: (Boolean) -> Unit
) : com.github.sandroln.core.SimpleTextWatcher() {

    override fun afterTextChanged(s: Editable) {
        val text = s.toString()
        enableCreateButton.invoke(text.length >= minimumLength)
    }

    companion object {
        private const val minimumLength: Int = 3
    }
}