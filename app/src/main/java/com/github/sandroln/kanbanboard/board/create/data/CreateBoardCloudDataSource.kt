package com.github.sandroln.kanbanboard.board.create.data

import com.github.sandroln.kanbanboard.boards.data.BoardCloud
import com.github.sandroln.kanbanboard.boards.presentation.BoardInfo
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.login.data.MyUser
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface CreateBoardCloudDataSource {

    suspend fun createBoard(name: String): BoardInfo

    class Base(
        private val myUser: MyUser,
        private val provideDatabase: ProvideDatabase
    ) : CreateBoardCloudDataSource {

        override suspend fun createBoard(name: String): BoardInfo {
            val myId = myUser.id()
            val reference = provideDatabase.database().child("boards").push()
            val task = reference.setValue(BoardCloud(myId, name))
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