package com.github.sandroln.board.presentation

import com.github.sandroln.core.Communication
import com.github.sandroln.openedboard.TicketUi

internal interface ColumnTicketCommunication : Communication.Mutable<List<TicketUi>> {
    class Base : Communication.Regular<List<TicketUi>>(), ColumnTicketCommunication
}