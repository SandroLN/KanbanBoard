package com.github.sandroln.kanbanboard.board.presentation

import android.view.View
import android.widget.TextView

interface BoardUiState {

    fun map(errorView: View, errorTextView: TextView, progress: View)

    data class Error(private val message: String) : BoardUiState {

        override fun map(errorView: View, errorTextView: TextView, progress: View) {
            errorView.visibility = View.VISIBLE
            errorTextView.text = message
            progress.visibility = View.GONE
        }
    }

    object HideProgress : BoardUiState {
        override fun map(errorView: View, errorTextView: TextView, progress: View) {
            errorView.visibility = View.GONE
            progress.visibility = View.GONE
        }
    }

    object ShowProgress : BoardUiState {
        override fun map(errorView: View, errorTextView: TextView, progress: View) {
            errorView.visibility = View.GONE
            progress.visibility = View.VISIBLE
        }
    }
}