package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginCloudDataSource {

    suspend fun login()

    class Base(private val provideDatabase: ProvideDatabase) : LoginCloudDataSource {

        override suspend fun login() {
            val user = Firebase.auth.currentUser
            val uid = user!!.uid
            val email = user.email!!
            val displayName = user.displayName

            if (email.isNullOrEmpty())
                throw IllegalStateException("problem occurred while getting email")

            val result = provideDatabase.database().child("users")
                .child(uid)
                .setValue(UserProfileCloud(email, displayName))
            handleResult(result)
        }

        private suspend fun handleResult(value: Task<Void>): Unit =
            suspendCoroutine { continuation ->
                value.addOnSuccessListener {
                    continuation.resume(Unit)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
            }
    }
}

private data class UserProfileCloud(
    val mail: String = "",
    val name: String? = null
)