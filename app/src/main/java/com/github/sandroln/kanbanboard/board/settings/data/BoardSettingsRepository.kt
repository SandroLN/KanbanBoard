package com.github.sandroln.kanbanboard.board.settings.data

import com.github.sandroln.kanbanboard.board.main.data.BoardUser
import com.github.sandroln.kanbanboard.boards.data.ChosenBoardCache
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.login.data.MyUser
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface BoardSettingsRepository {

    suspend fun findUsers(userEmail: String): List<Pair<String, UserProfileCloud>>

    fun inviteUserToBoard(user: BoardUser)

    class Base(
        private val myUser: MyUser,
        private val chosenBoardCache: ChosenBoardCache.Read,
        private val provideDatabase: ProvideDatabase
    ) : BoardSettingsRepository {

        override suspend fun findUsers(userEmail: String): List<Pair<String, UserProfileCloud>> {
            val query = provideDatabase.database()
                .child("users")
                .orderByChild("mail")
                .startAt(userEmail)
                .limitToFirst(100)
            return handleQuery(query, userEmail)
        }

        override fun inviteUserToBoard(user: BoardUser) {
            val reference = provideDatabase.database()
                .child("boards-invitations")
                .push()
            reference.setValue(chosenBoardCache.read().invite(user))
        }

        private suspend fun handleQuery(query: Query, userEmail: String) =
            suspendCoroutine<List<Pair<String, UserProfileCloud>>> { continuation ->
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users = snapshot.children.mapNotNull {
                            Pair(it.key!!, it.getValue(UserProfileCloud::class.java)!!)
                        }.filter { it.second.mail.startsWith(userEmail, true) }
                        continuation.resume(users.filter { it.first != myUser.id() })
                    }

                    override fun onCancelled(error: DatabaseError) =
                        continuation.resume(emptyList())
                })
            }
    }
}