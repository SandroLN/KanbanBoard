package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface Invitations {

    interface Callback {

        fun provideInvitations(list: List<Pair<String, OtherBoardCloud>>)

        object Empty : Callback {

            override fun provideInvitations(list: List<Pair<String, OtherBoardCloud>>) = Unit
        }
    }

    interface CloudDataSource {

        fun init(callback: Callback)

        fun handle(userId: String)

        class Base(
            private val provideDatabase: ProvideDatabase
        ) : CloudDataSource {

            private var callback: Callback = Callback.Empty

            private val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) = callback.provideInvitations(
                    snapshot.children.mapNotNull {
                        Pair(
                            it.key!!,
                            it.getValue(OtherBoardCloud::class.java)!!
                        )
                    }
                )

                override fun onCancelled(error: DatabaseError) = Unit//todo
            }

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun handle(userId: String) {
                provideDatabase.database()
                    .child("boards-invitations")
                    .orderByChild("memberId")
                    .equalTo(userId)
                    .addValueEventListener(listener)
            }
        }
    }
}