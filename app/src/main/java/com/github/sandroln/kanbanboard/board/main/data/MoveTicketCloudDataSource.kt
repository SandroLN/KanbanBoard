package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.kanbanboard.board.main.presentation.Column
import com.github.sandroln.kanbanboard.service.Service

interface MoveTicketCloudDataSource : MoveTicket {

    class Base(private val service: Service) : MoveTicketCloudDataSource {

        override fun moveTicket(id: String, newColumn: Column) =
            service.updateField("tickets", id, "columnId", newColumn.cloudValue())
    }
}