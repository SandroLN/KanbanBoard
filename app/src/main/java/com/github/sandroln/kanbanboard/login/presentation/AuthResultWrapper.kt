package com.github.sandroln.kanbanboard.login.presentation

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

interface AuthResultWrapper {

    fun isSuccessful(): Boolean

    fun task(): Task<GoogleSignInAccount>

    class Base(private val activityResult: ActivityResult) : AuthResultWrapper {

        override fun isSuccessful() = activityResult.resultCode == Activity.RESULT_OK

        override fun task(): Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

    }
}