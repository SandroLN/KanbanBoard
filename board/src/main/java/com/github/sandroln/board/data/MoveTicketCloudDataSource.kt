package com.github.sandroln.board.data

import com.github.sandroln.cloudservice.Service
import com.github.sandroln.openedboard.Column
import com.github.sandroln.openedboard.MoveTicket

internal interface MoveTicketCloudDataSource : MoveTicket {

    class Base(private val service: Service) : MoveTicketCloudDataSource {

        override fun moveTicket(id: String, newColumn: Column) =
            service.updateField("tickets", id, "columnId", newColumn.cloudValue())
    }
}