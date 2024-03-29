package com.github.sandroln.editticket.data

import com.github.sandroln.cloudservice.ReferenceWrapper
import com.github.sandroln.cloudservice.Service
import com.github.sandroln.editticket.presentation.DeleteTicket
import com.github.sandroln.editticket.presentation.EditTicketCallback
import com.github.sandroln.editticket.presentation.EditTicketCommunication
import com.github.sandroln.openedboard.Assignee
import com.github.sandroln.openedboard.EditTicketIdCache
import com.github.sandroln.openedboard.TicketCloud


internal interface EditTicketCloudDataSource : EditTicketCloudActions, ChangeSingleField {

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

internal interface EditTicketCloudActions : DeleteTicket, com.github.sandroln.core.SimpleInit