package com.github.sandroln.kanbanboard.boards.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface BoardCloudDataSource {

    interface Callback {

        fun provideBoard(boardId: String, data: Pair<String, BoardCloud>)

        object Empty : Callback {

            override fun provideBoard(boardId: String, data: Pair<String, BoardCloud>) = Unit
        }
    }

    fun init(callback: Callback)

    fun fetch(id: String, boardId: String)

    class Base(
        private val provideDatabase: ProvideDatabase
    ) : BoardCloudDataSource {

        private var callback: Callback = Callback.Empty

        override fun init(callback: Callback) {
            this.callback = callback
        }

        override fun fetch(id: String, boardId: String) = provideDatabase.database()
            .child("boards")
            .orderByKey()
            .equalTo(boardId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) =
                    callback.provideBoard(boardId, snapshot.children.firstNotNullOf {
                        Pair(id, it.getValue(BoardCloud::class.java)!!)
                    })

                override fun onCancelled(error: DatabaseError) = Unit//todo
            })
    }
}