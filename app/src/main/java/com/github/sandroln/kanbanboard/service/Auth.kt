package com.github.sandroln.kanbanboard.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface Auth {

    suspend fun auth(idToken: String): Pair<Boolean, String>

    class Base : Auth {

        override suspend fun auth(idToken: String): Pair<Boolean, String> {
            val firebaseCredential =
                GoogleAuthProvider.getCredential(idToken, null)
            val signInWithCredential =
                Firebase.auth.signInWithCredential(firebaseCredential)
            return handleInner(signInWithCredential)
        }

        private suspend fun handleInner(task: Task<AuthResult>): Pair<Boolean, String> =
            suspendCoroutine { cont ->
                task.addOnSuccessListener {
                    cont.resume(Pair(true, ""))
                }.addOnFailureListener {
                    cont.resume(Pair(false, it.message ?: "error"))
                }
            }
    }
}