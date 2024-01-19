package com.github.sandroln.kanbanboard.board.settings.data

import com.github.sandroln.kanbanboard.boards.data.OtherBoardCloud
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface Invitations {

    interface Callback {

        fun provideInvitations(invitedMemberIds: List<String>)

        object Empty : Callback {

            override fun provideInvitations(invitedMemberIds: List<String>) = Unit
        }
    }

    interface CloudDataSource {

        interface Init {

            fun init(callback: Callback)
        }

        interface Handle {

            fun handle(boardId: String)
        }

        interface Mutable : Init, Handle

        class Base(private val provideDatabase: ProvideDatabase) : Mutable {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun handle(boardId: String) {
                provideDatabase.database()
                    .child("boards-invitations")
                    .orderByChild("boardId")
                    .equalTo(boardId)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val list = snapshot.children.mapNotNull {
                                it.getValue(OtherBoardCloud::class.java)?.memberId
                            }
                            callback.provideInvitations(list)
                        }

                        override fun onCancelled(error: DatabaseError) = Unit //todo
                    })
            }
        }
    }
}