package com.github.sandroln.kanbanboard.board.presentation

import android.view.View
import android.widget.TextView

interface BoardUiState {

    fun map(boardNameTextView: TextView, settingsButton: View) = Unit
    fun map(errorView: View, errorTextView: TextView, progress: View) {
        errorView.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    data class Initial(
        private val boardName: String,
        private val showSettings: Boolean
    ) : BoardUiState {

        override fun map(boardNameTextView: TextView, settingsButton: View) {
            boardNameTextView.text = boardName
            settingsButton.visibility = if (showSettings) View.VISIBLE else View.GONE
        }
    }

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

    object ShowProgress : BoardUiState
}