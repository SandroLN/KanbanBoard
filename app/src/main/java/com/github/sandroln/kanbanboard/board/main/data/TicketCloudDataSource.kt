package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.core.ProvideError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface Tickets {

    interface Callback {

        fun provideTickets(tickets: List<Pair<String, TicketCloud>>)

        object Empty : Callback {
            override fun provideTickets(tickets: List<Pair<String, TicketCloud>>) = Unit
        }
    }

    interface CloudDataSource {
        fun init(callback: Callback)

        fun ticketsForBoard(boardId: String)

        class Base(
            private val provideError: ProvideError,
            private val provideDatabase: ProvideDatabase,
        ) : CloudDataSource {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            private val ticketsListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children.mapNotNull {
                        Pair(it.key!!, it.getValue(TicketCloud::class.java)!!)
                    }
                    callback.provideTickets(data)
                }

                override fun onCancelled(error: DatabaseError) = provideError.error(error.message)
            }

            override fun ticketsForBoard(boardId: String) {
                val query = provideDatabase.database()
                    .child("tickets")
                    .orderByChild("boardId")
                    .equalTo(boardId)
                query.removeEventListener(ticketsListener)
                query.addValueEventListener(ticketsListener)
            }
        }
    }
}