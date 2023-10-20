package com.github.sandroln.kanbanboard.board.data

import com.github.sandroln.kanbanboard.board.presentation.Column
import com.github.sandroln.kanbanboard.core.ProvideDatabase

interface MoveTicketCloudDataSource : MoveTicket {

    class Base(
        private val provideDatabase: ProvideDatabase,
    ) : MoveTicketCloudDataSource {

        override fun moveTicket(id: String, newColumn: Column) {
            val value = newColumn.cloudValue()
            provideDatabase.database()
                .child("tickets")
                .child(id)
                .child("columnId")
                .setValue(value)
        }
    }
}