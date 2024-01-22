package com.github.sandroln.cloudservice

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

interface MyUser : UserNotLoggedIn {

    fun checkDataInvalid(): Boolean

    fun signOut()

    fun id(): String

    fun profile(): Pair<String, String>

    fun userProfileCloud(): Pair<String, String>

    class Base(private val navigation: NavigateToLoginScreen) : MyUser {

        override fun checkDataInvalid(): Boolean {
            val notValid = userNotLoggedIn()
            if (notValid)
                navigation.navigateToLoginScreen()
            return notValid
        }

        override fun signOut() {
            Firebase.auth.signOut()
            navigation.navigateToLoginScreen()
        }

        override fun id() = if (checkDataInvalid()) "" else user()!!.uid

        override fun profile() = if (checkDataInvalid())
            Pair("", "")
        else {
            val user = user()!!
            Pair(
                user.email!!,
                user.displayName ?: ""
            )
        }

        override fun userProfileCloud(): Pair<String, String> {
            val user = user() ?: throw IllegalStateException("user null")
            val email = user.email
            if (email.isNullOrEmpty())
                throw IllegalStateException("problem occurred while getting email")
            val displayName = user.displayName ?: email
            return Pair(email, displayName)
        }

        override fun userNotLoggedIn() = user() == null

        private fun user(): FirebaseUser? = Firebase.auth.currentUser
    }
}

interface NavigateToLoginScreen {
    fun navigateToLoginScreen()
}