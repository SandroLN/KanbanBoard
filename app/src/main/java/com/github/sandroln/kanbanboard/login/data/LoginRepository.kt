package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.login.presentation.AuthResultWrapper
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface LoginRepository : UserNotLoggedIn {

    suspend fun handleResult(authResult: AuthResultWrapper): LoginResult

    class Base(
        private val myUser: MyUser,
        private val loginCloudDataSource: LoginCloudDataSource
    ) : LoginRepository {

        override fun userNotLoggedIn() = myUser.userNotLoggedIn()

        override suspend fun handleResult(authResult: AuthResultWrapper) =
            if (authResult.isSuccessful()) {
                try {
                    val task = authResult.task()
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                    val idToken = account.idToken

                    if (idToken == null) {
                        LoginResult.Failed("something went wrong")
                    } else {
                        val firebaseCredential =
                            GoogleAuthProvider.getCredential(idToken, null)
                        val signInWithCredential =
                            Firebase.auth.signInWithCredential(firebaseCredential)
                        val (successful, errorMessage) = handleInner(signInWithCredential)
                        if (successful) {
                            loginCloudDataSource.login()
                            LoginResult.Success
                        } else
                            LoginResult.Failed(errorMessage)
                    }
                } catch (e: Exception) {
                    LoginResult.Failed(e.message ?: "something went wrong")
                }
            } else
                LoginResult.Failed("not successful to login")

        private suspend fun handleInner(task: Task<AuthResult>): Pair<Boolean, String> =
            suspendCoroutine { cont ->
                task
                    .addOnSuccessListener {
                        cont.resume(Pair(true, ""))
                    }.addOnFailureListener {
                        cont.resume(Pair(false, it.message ?: "error"))
                    }
            }
    }
}

interface UserNotLoggedIn {

    fun userNotLoggedIn(): Boolean
}