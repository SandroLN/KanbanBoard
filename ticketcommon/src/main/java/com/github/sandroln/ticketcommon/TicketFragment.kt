package com.github.sandroln.ticketcommon

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.core.BaseFragment
import com.github.sandroln.core.SimpleTextWatcher
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardUser
import com.github.sandroln.openedboard.MembersAdapter
import com.github.sandroln.openedboard.UpdateQuery
import com.github.sandroln.openedboard.setTextCorrect
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

abstract class TicketFragment<T : TicketViewModel>(layoutId: Int) : BaseFragment<T>(layoutId) {

    protected abstract val backButtonId: Int
    protected abstract val actionButtonId: Int
    protected lateinit var colorsViewGroup: ColorsViewGroup
    protected lateinit var titleEditText: TextInputEditText
    protected lateinit var descriptionEditText: TextInputEditText
    protected lateinit var chooseColumnViewGroup: ChooseColumnViewGroup

    protected val assigneeEditTextId = R.id.assigneeEditText
    protected val boardMembersRecyclerViewId = R.id.boardMembersRecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //region init views
        colorsViewGroup = view.findViewById(R.id.colorsViewGroup)
        titleEditText = view.findViewById(R.id.createTicketEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        chooseColumnViewGroup = view.findViewById(R.id.chooseColumnViewGroup)
        val backButton = view.findViewById<View>(backButtonId)
        val actionButton = view.findViewById<Button>(actionButtonId)
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