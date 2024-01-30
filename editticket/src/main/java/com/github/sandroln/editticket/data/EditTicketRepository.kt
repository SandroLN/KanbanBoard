package com.github.sandroln.editticket.data

import com.github.sandroln.editticket.presentation.DeleteTicket
import com.github.sandroln.editticket.presentation.TicketChange
import com.github.sandroln.openedboard.EditTicketIdCache

internal interface EditTicketRepository : DeleteTicket, com.github.sandroln.core.SimpleInit {

    fun makeChanges(changes: List<TicketChange>)

    fun clearTicketId()

    class Base(
        private val editTicketIdCache: EditTicketIdCache.Save,
        private val changeTicketFields: ChangeTicketFields,
        private val editTicketCloudDataSource: EditTicketCloudActions,
    ) : EditTicketRepository {

        override fun init() = editTicketCloudDataSource.init()

        override fun makeChanges(changes: List<TicketChange>) = changes.forEach {
            it.applyChanges(changeTicketFields)
        }

        override fun deleteTicket() = editTicketCloudDataSource.deleteTicket()

        override fun clearTicketId() = editTicketIdCache.save("")
    }
}