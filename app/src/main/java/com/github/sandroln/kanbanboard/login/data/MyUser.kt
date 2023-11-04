package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.login.presentation.LoginScreen
import com.github.sandroln.kanbanboard.main.NavigationCommunication
import com.github.sandroln.kanbanboard.profile.presentation.ProfileUiState
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface MyUser : UserNotLoggedIn {

    fun checkDataInvalid(): Boolean

    fun signOut()

    fun id(): String

    fun profile(): ProfileUiState

    fun userProfileCloud(): UserProfileCloud

    class Base(private val navigationCommunication: NavigationCommunication.Update) : MyUser {

        override fun checkDataInvalid(): Boolean {
            val notValid = userNotLoggedIn()
            if (notValid)
                navigationCommunication.map(LoginScreen)
            return notValid
        }

        override fun signOut() {
            Firebase.auth.signOut()
            navigationCommunication.map(LoginScreen)
        }

        override fun id() = if (checkDataInvalid()) "" else user()!!.uid

        override fun profile() = if (checkDataInvalid())
            ProfileUiState.Empty
        else {
            val user = user()!!
            ProfileUiState.Base(
                user.email!!,
                user.displayName ?: ""
            )
        }

        override fun userProfileCloud(): UserProfileCloud {
            val user = user() ?: throw IllegalStateException("user null")
            val email = user.email
            if (email.isNullOrEmpty())
                throw IllegalStateException("problem occurred while getting email")
            val displayName = user.displayName ?: email
            return UserProfileCloud(email, displayName)
        }

        override fun userNotLoggedIn() = user() == null

        private fun user(): FirebaseUser? = Firebase.auth.currentUser
    }
}