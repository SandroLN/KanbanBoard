package com.github.sandroln.kanbanboard.profile.presentation

import android.widget.TextView
import com.github.sandroln.kanbanboard.R

interface ProfileUiState {

    fun show(textView: TextView)

    class Base(
        private val email: String,
        private val name: String
    ) : ProfileUiState {

        override fun show(textView: TextView) {
            textView.text = textView.context.getString(R.string.my_email, name, email)
        }
    }

    object Empty : ProfileUiState {
        override fun show(textView: TextView) = Unit
    }
}