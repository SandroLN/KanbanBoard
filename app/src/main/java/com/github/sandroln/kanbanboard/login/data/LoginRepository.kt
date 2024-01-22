package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.cloudservice.Auth
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.UserNotLoggedIn
import com.github.sandroln.kanbanboard.login.presentation.AuthResultWrapper
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException

interface LoginRepository : UserNotLoggedIn {

    suspend fun handleResult(authResult: AuthResultWrapper): LoginResult

    class Base(
        private val auth: Auth,
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
                        val (successful, errorMessage) = auth.auth(idToken)
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
    }
}