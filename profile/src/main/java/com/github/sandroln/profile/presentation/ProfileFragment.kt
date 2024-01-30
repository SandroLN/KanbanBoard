package com.github.sandroln.profile.presentation

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.github.sandroln.core.BaseFragment
import com.github.sandroln.profile.R

internal class ProfileFragment : BaseFragment<ProfileViewModel>(R.layout.fragment_profile) {

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