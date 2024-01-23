package com.github.sandroln.kanbanboard.profile.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.github.sandroln.kanbanboard.R

class ProfileFragment : com.github.sandroln.core.BaseFragment<ProfileViewModel>(R.layout.fragment_profile) {

    override val viewModelClass = ProfileViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.emailTextView)
        view.findViewById<View>(R.id.signOutButton).setOnClickListener {
            viewModel.signOut()
        }
        view.findViewById<View>(R.id.backButton).setOnClickListener {
            viewModel.goBack()
        }
        viewModel.observe(this) {
            it.show(textView)
        }
        viewModel.init(savedInstanceState == null)
    }
}