package com.github.sandroln.kanbanboard.login.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface LoginCloudDataSource {

    suspend fun login()

    class Base(
        private val myUser: MyUser,
        private val provideDatabase: ProvideDatabase
    ) : LoginCloudDataSource {

        override suspend fun login() {
            val id = myUser.id()
            val userProfile = myUser.userProfileCloud()
            val result = provideDatabase.database().child("users")
                .child(id)
                .setValue(userProfile)
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

data class UserProfileCloud(
    val mail: String = "",
    val name: String = ""
)