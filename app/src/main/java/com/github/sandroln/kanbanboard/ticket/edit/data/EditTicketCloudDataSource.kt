package com.github.sandroln.kanbanboard.ticket.edit.data

import com.github.sandroln.kanbanboard.board.main.data.Assignee
import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.board.main.data.TicketCloud
import com.github.sandroln.kanbanboard.core.ProvideDatabase
import com.github.sandroln.kanbanboard.core.SimpleInit
import com.github.sandroln.kanbanboard.ticket.edit.presentation.DeleteTicket
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCallback
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCommunication
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


interface EditTicketCloudDataSource : EditTicketCloudActions, ChangeSingleField {

    class Base(
        private val assigneeName: Assignee.Name,
        private val editTicketCommunication: EditTicketCommunication.Update,
        provideDatabase: ProvideDatabase,
        editTicketIdCache: EditTicketIdCache.Read,
    ) : EditTicketCloudDataSource {

        private val id = editTicketIdCache.read()

        private val reference = provideDatabase.database()
            .child("tickets")
            .child(id)

        override fun init() {
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ticketCloud = snapshot.getValue(TicketCloud::class.java)
                    ticketCloud?.toUi(id, assigneeName)?.let {
                        editTicketCommunication.map(EditTicketCallback.Update(it))
                    } ?: editTicketCommunication.map(EditTicketCallback.Delete)
                }

                override fun onCancelled(error: DatabaseError) = Unit//todo
            })
        }

        override fun change(key: String, newValue: Any) {
            reference.child(key).setValue(newValue)
        }

        override fun deleteTicket() {
            reference.removeValue()
        }
    }
}

interface EditTicketCloudActions : DeleteTicket, SimpleInit