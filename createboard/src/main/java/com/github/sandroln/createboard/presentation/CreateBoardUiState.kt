package com.github.sandroln.createboard.presentation

import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout

internal interface CreateBoardUiState {

    fun show(progressBar: View, createButton: Button, textInputLayout: TextInputLayout)

    abstract class Abstract(
        private val progressVisible: Boolean,
        private val createButtonEnabled: Boolean,
        private val error: String = ""
    ) : CreateBoardUiState {

        override fun show(
            progressBar: View,
            createButton: Button,
            textInputLayout: TextInputLayout
        ) {
            progressBar.visibility = if (progressVisible) View.VISIBLE else View.GONE
            createButton.isEnabled = createButtonEnabled
            textInputLayout.error = error
            textInputLayout.isErrorEnabled = error.isNotEmpty()
        }
    }

    object Progress : Abstract(true, false)

    object CanCreateBoard : Abstract(false, true)
    object CanNotCreateBoard : Abstract(false, false)

    data class BoardAlreadyExists(private val value: String) : Abstract(false, false, value)

    data class Error(private val errorMessage: String) : Abstract(false, false, errorMessage)
}