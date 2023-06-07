package com.github.sandroln.kanbanboard.login.presentation

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.github.sandroln.kanbanboard.R
import com.github.sandroln.kanbanboard.core.ManageResource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

interface LoginUiState {

    fun handle(launcher: ActivityResultLauncher<Intent>, activity: Activity) = Unit
    fun update(loginButton: View, progressBar: View, errorTextView: TextView) = Unit

    object Initial : LoginUiState {

        override fun update(loginButton: View, progressBar: View, errorTextView: TextView) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.text = ""
        }
    }

    data class Error(private val message: String) : LoginUiState {

        override fun update(loginButton: View, progressBar: View, errorTextView: TextView) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            errorTextView.text = message
        }
    }

    data class Auth(private val manageResource: ManageResource) : LoginUiState {

        private val option by lazy {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(manageResource.string(R.string.your_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        }

        override fun handle(launcher: ActivityResultLauncher<Intent>, activity: Activity) {
            launcher.launch(GoogleSignIn.getClient(activity, option).signInIntent)
        }

        override fun update(loginButton: View, progressBar: View, errorTextView: TextView) {
            loginButton.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            errorTextView.text = ""
        }
    }
}