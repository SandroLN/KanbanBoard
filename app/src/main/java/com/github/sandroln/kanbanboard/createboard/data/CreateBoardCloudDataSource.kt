package com.github.sandroln.kanbanboard.createboard.data

import com.github.sandroln.kanbanboard.boards.data.BoardCloud
import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CreateBoardCloudDataSource {

    suspend fun createBoard(name: String): BoardInfo

    class Base(private val provideDatabase: ProvideDatabase) : CreateBoardCloudDataSource {

        override suspend fun createBoard(name: String): BoardInfo {
            val myUid = Firebase.auth.currentUser!!.uid
            val reference = provideDatabase.database().child("boards").push()
            val task = reference.setValue(BoardCloud(myUid, name))
            handleResult(task)
            return BoardInfo(reference.key!!, name, true)
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