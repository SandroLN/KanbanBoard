package com.github.sandroln.login.presentation

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import com.github.sandroln.core.ManageResource
import com.github.sandroln.login.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

internal interface LoginUiState {

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

        private val options by lazy {
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(manageResource.string(R.string.your_web_client_id))
                .requestEmail()
                .requestProfile()
                .build()
        }

        override fun handle(launcher: ActivityResultLauncher<Intent>, activity: Activity) {
            launcher.launch(GoogleSignIn.getClient(activity, options).signInIntent)
        }

        override fun update(loginButton: View, progressBar: View, errorTextView: TextView) {
            loginButton.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
            errorTextView.text = ""
        }
    }
}