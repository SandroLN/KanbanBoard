package com.github.sandroln.kanbanboard.ticket.edit.data

import com.github.sandroln.cloudservice.ReferenceWrapper
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.kanbanboard.board.main.data.Assignee
import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.board.main.data.TicketCloud
import com.github.sandroln.kanbanboard.ticket.edit.presentation.DeleteTicket
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCallback
import com.github.sandroln.kanbanboard.ticket.edit.presentation.EditTicketCommunication


interface EditTicketCloudDataSource : EditTicketCloudActions, ChangeSingleField {

    class Base(
        private val assigneeName: Assignee.Name,
        private val editTicketCommunication: EditTicketCommunication.Update,
        service: Service,
        editTicketIdCache: EditTicketIdCache.Read
    ) : EditTicketCloudDataSource {

        private val id = editTicketIdCache.read()

        private val referenceWrapper =
            service.referenceWrapper("tickets", id, TicketCloud::class.java)

        override fun init() {
            referenceWrapper.startListen(object : ReferenceWrapper.Callback<TicketCloud?> {
                override fun provide(obj: TicketCloud?) {
                    val id = id
                    obj?.toUi(id, assigneeName)?.let {
                        editTicketCommunication.map(EditTicketCallback.Update(it))
                    } ?: editTicketCommunication.map(EditTicketCallback.Delete)
                }

                override fun error(message: String) = Unit//todo
            })
        }

        override fun change(key: String, newValue: Any) {
            referenceWrapper.change(key, newValue)
        }

        override fun deleteTicket() {
            referenceWrapper.delete()
        }
    }
}

interface EditTicketCloudActions : DeleteTicket, com.github.sandroln.core.SimpleInit