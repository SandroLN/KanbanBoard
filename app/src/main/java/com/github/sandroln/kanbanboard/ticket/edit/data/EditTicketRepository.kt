package com.github.sandroln.kanbanboard.ticket.edit.data

import com.github.sandroln.kanbanboard.board.main.data.EditTicketIdCache
import com.github.sandroln.kanbanboard.core.SimpleInit
import com.github.sandroln.kanbanboard.ticket.edit.presentation.DeleteTicket
import com.github.sandroln.kanbanboard.ticket.edit.presentation.TicketChange

interface EditTicketRepository : DeleteTicket, SimpleInit {

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