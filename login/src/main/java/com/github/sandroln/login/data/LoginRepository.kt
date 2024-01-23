package com.github.sandroln.login.data

import com.github.sandroln.cloudservice.Auth
import com.github.sandroln.cloudservice.MyUser
import com.github.sandroln.cloudservice.UserNotLoggedIn
import com.github.sandroln.login.presentation.AuthResultWrapper

internal interface LoginRepository : UserNotLoggedIn {

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
                    val idToken = authResult.idToken()

                    if (idToken.isEmpty()) {
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