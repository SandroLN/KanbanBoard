package com.github.sandroln.boardssettings.presentation

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.sandroln.boardssettings.R
import com.github.sandroln.core.BaseFragment
import com.github.sandroln.openedboard.AssignUser
import com.github.sandroln.openedboard.BoardUser
import com.google.android.material.textfield.TextInputEditText

internal class BoardSettingsFragment :
    BaseFragment<BoardSettingsViewModel>(R.layout.fragment_board_settings) {

    override val viewModelClass = BoardSettingsViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<View>(R.id.backButton)
        val addUserEditText = view.findViewById<TextInputEditText>(R.id.addUserEditText)
        val foundUsersRecyclerView = view.findViewById<RecyclerView>(R.id.foundUsersRecyclerView)
        val currentMembersRecyclerView =
            view.findViewById<RecyclerView>(R.id.boardMembersRecyclerView)
        addUserEditText.addTextChangedListener(FindUsersWatcher(viewModel))

        backButton.setOnClickListener {
            viewModel.goBack()
        }

        val foundUsersAdapter = BoardSettingsMembersAdapter(object : AssignUser {
            override fun assign(user: BoardUser) {
                addUserEditText.setText("")
                viewModel.assign(user)
            }
        })
        foundUsersRecyclerView.adapter = foundUsersAdapter
        viewModel.observeFoundUsers(this) {
            foundUsersAdapter.map(it)
        }

        val currentMembersAdapter = BoardSettingsMembersAdapter(AssignUser.Empty)
        currentMembersRecyclerView.adapter = currentMembersAdapter

        viewModel.observe(this) {
            currentMembersAdapter.map(it)
        }
    }
}

private class FindUsersWatcher(private val findUsers: FindUsers) : com.github.sandroln.core.SimpleTextWatcher() {
    override fun afterTextChanged(s: Editable) = findUsers.findUsers(s.toString())
}