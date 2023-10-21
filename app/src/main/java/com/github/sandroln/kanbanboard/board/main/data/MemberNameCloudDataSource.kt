package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.core.ProvideError
import com.github.sandroln.kanbanboard.login.data.UserProfileCloud
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface MemberName {

    interface Callback {

        fun provideMember(user: UserProfileCloud, userId: String)

        object Empty : Callback {
            override fun provideMember(user: UserProfileCloud, userId: String) = Unit
        }
    }

    interface CloudDataSource {
        fun init(callback: Callback)

        fun handle(memberId: String)

        class Base(
            private val provideError: ProvideError,
            private val provideDatabase: ProvideDatabase,
        ) : CloudDataSource {
            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            override fun handle(memberId: String) {
                val query = provideDatabase.database()
                    .child("users")
                    .orderByKey()
                    .equalTo(memberId)

                val userListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userCloud = snapshot.children.firstNotNullOf {
                            Pair(
                                it.key!!,
                                it.getValue(UserProfileCloud::class.java)
                            )
                        }
                        callback.provideMember(userCloud.second!!, userCloud.first)
                    }

                    override fun onCancelled(error: DatabaseError) =
                        provideError.error(error.message)
                }
                query.addListenerForSingleValueEvent(userListener)
            }
        }
    }
}