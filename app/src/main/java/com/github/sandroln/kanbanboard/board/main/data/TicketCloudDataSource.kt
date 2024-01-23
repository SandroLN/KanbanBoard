package com.github.sandroln.kanbanboard.board.main.data

import com.github.sandroln.cloudservice.Service

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
            private val provideError: com.github.sandroln.core.ProvideError,
            private val service: Service,
        ) : CloudDataSource {

            private var callback: Callback = Callback.Empty

            override fun init(callback: Callback) {
                this.callback = callback
            }

            private val listener = object : Service.Callback<TicketCloud> {
                override fun provide(obj: List<Pair<String, TicketCloud>>) =
                    callback.provideTickets(obj)

                override fun error(message: String) = provideError.error(message)
            }

            override fun ticketsForBoard(boardId: String) {
                service.getByQueryAsync(
                    "tickets",
                    "boardId",
                    boardId,
                    TicketCloud::class.java,
                    listener
                )
            }
        }
    }
}