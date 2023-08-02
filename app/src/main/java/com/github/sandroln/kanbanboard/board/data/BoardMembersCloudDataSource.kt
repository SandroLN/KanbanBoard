package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.boards.data.OtherBoardCloud
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.core.ProvideError
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

interface BoardMembers {

    interface Callback {

        fun provideAllMembersIds(members: Set<String>)

        object Empty : Callback {
            override fun provideAllMembersIds(members: Set<String>) = Unit
        }
    }

    interface CloudDataSource {

        fun init(callback: Callback)

        fun provideAllMembers(boardId: String, isMyBoard: Boolean, ownerId: String)


        class Base(
            private val provideError: ProvideError,
            private val provideDatabase: ProvideDatabase,
        ) : CloudDataSource {
            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun provideAllMembers(boardId: String, isMyBoard: Boolean, ownerId: String) {
                val boardMembersIds = mutableSetOf<String>()
                boardMembersIds.add(if (isMyBoard) Firebase.auth.currentUser!!.uid else ownerId)

                val membersQuery = provideDatabase.database()
                    .child("boards-members")
                    .orderByChild("boardId")
                    .equalTo(boardId)

                val membersListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val memberIds = snapshot.children.mapNotNull {
                            it.getValue(OtherBoardCloud::class.java)?.memberId
                        }
                        boardMembersIds.addAll(memberIds)
                        callback.provideAllMembersIds(boardMembersIds)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        provideError.error(error.message)
                }
                membersQuery.removeEventListener(membersListener)
                membersQuery.addValueEventListener(membersListener)
            }
        }
    }
}