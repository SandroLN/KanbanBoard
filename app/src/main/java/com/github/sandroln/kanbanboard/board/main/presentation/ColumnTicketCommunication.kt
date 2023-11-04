package com.github.sandroln.kanbanboard.board.main.presentation

import com.github.sandroln.kanbanboard.core.Communication

interface ColumnTicketCommunication : Communication.Mutable<List<TicketUi>> {
    class Base : Communication.Regular<List<TicketUi>>(), ColumnTicketCommunication
}